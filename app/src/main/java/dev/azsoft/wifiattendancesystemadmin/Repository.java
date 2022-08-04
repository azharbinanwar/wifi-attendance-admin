package dev.azsoft.wifiattendancesystemadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dev.azsoft.wifiattendancesystemadmin.databasehelper.SharedPrefs;
import dev.azsoft.wifiattendancesystemadmin.global.Const;
import dev.azsoft.wifiattendancesystemadmin.global.PermissionStatus;
import dev.azsoft.wifiattendancesystemadmin.interfaces.OnComplete;
import dev.azsoft.wifiattendancesystemadmin.models.AdminUser;
import dev.azsoft.wifiattendancesystemadmin.models.Attendance;
import dev.azsoft.wifiattendancesystemadmin.models.CreateUserResponse;
import dev.azsoft.wifiattendancesystemadmin.models.Employee;

public class Repository {
    private static Repository repository;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private String url = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + Const.API_KEY;


    public static Repository getInstance() {
        if (repository == null)
            repository = new Repository();

        return repository;
    }

    public void onLogIn(Boolean fetchUserDetails, String email, String password, OnComplete onComplete) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AuthResult authResult = task.getResult();
                if (fetchUserDetails) {
                    fetchUserDetails(authResult.getUser().getUid(), onComplete);
                } else {
                    onComplete.onResult(task.getResult());
                }
            }
        }).addOnFailureListener(e -> onComplete.onResult(e));
    }

    public void fetchUserDetails(String uid, OnComplete onComplete) {
        firestore.collection(Const.ADMINS).document(uid).get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();
            AdminUser user = snapshot.toObject(AdminUser.class);
            onComplete.onResult(user);
        }).addOnFailureListener(e -> onComplete.onResult(e));
    }

    public void fetchEmployeeDetails(String uid, OnComplete onComplete) {
        firestore.collection(Const.EMPLOYEES).document(uid).get().addOnCompleteListener(task -> {
            onComplete.onResult(task.getResult());
        }).addOnFailureListener(e -> onComplete.onResult(e));
    }

    public void onCreateEmployee(Employee employee, RequestQueue requestQueue, OnComplete onComplete) {
        requestQueue.add(new StringRequest(Request.Method.POST,
                url,
                (String response) -> {
                    CreateUserResponse userResponse = new Gson().fromJson(response, CreateUserResponse.class);
                    employee.setUid(userResponse.getLocalId());
                    onCreateDocument(employee.getUid(), employee, Const.EMPLOYEES, onComplete);
                },
                onComplete::onResult
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", employee.getEmail());
                params.put("password", employee.getPassword());
                params.put("returnSecureToken", "true");
                return params;
            }
        });

    }

    public void onCreateDocument(String uid, Object object, String collection, OnComplete onComplete) {
        firestore.collection(collection).document(uid).set(object).addOnCompleteListener(
                task -> onComplete.onResult(object)
        ).addOnFailureListener(onComplete::onResult);
    }

    public void fetchAllDocuments(String collection, OnComplete onComplete) {
        firestore.collection(collection).get().addOnCompleteListener(
                task -> onComplete.onResult(task.getResult())
        ).addOnFailureListener(onComplete::onResult);
    }

//    public void listenEmployeesAttendance(String collection, OnComplete onComplete) {
//        List<Attendance> attendanceList = new ArrayList<>();
//        firestore.collection(collection).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                for (int i = 0; i < value.size(); i++) {
//                    System.out.println("Repository.onEvent " + value.size());
//                    DocumentSnapshot snapshot = value.getDocuments().get(i);
//                    String id = snapshot.getString("id");
//                    String uid = snapshot.getString("uid");
//                    long workingMinutes = snapshot.getLong("workingMinutes");
//                    String networkName = snapshot.getString("networkName");
//
//                    fetchUserDetails(uid, new OnComplete() {
//                        @Override
//                        public void onResult(Object ob) {
//                            if (ob instanceof DocumentSnapshot) {
//                                DocumentSnapshot snapshot1 = (DocumentSnapshot) ob;
//                                String userName = snapshot1.getString("userName");
//                                String profilePhoto = snapshot1.getString("profilPhoto");
//                                Attendance attendance = new Attendance(
//                                        id,
//                                        uid,
//                                        workingMinutes,
//                                        networkName,
//                                        userName,
//                                        profilePhoto
//                                );
//                                attendanceList.add(attendance);
//                            }
//                        }
//                                        onComplete.onResult(attendanceList);
//
//                    });
//
//
//                }
//            }
//        });
//    }


    public void signOut() {
        firebaseAuth.signOut();
        SharedPrefs.getInstance().clear();
    }


    public void onPermissionHandler(Context context, String permission, OnComplete onComplete) {
        Dexter
                .withContext(context)
                .withPermission(permission)
                .withListener(new PermissionListener() {
                                  @Override
                                  public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                      onComplete.onResult(PermissionStatus.permissionGranted);
                                  }

                                  @Override
                                  public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                      if (permissionDeniedResponse.isPermanentlyDenied()) {
                                          onComplete.onResult(PermissionStatus.permissionDeniedPermanently);
                                      } else {
                                          onComplete.onResult(PermissionStatus.permissionDenied);
                                      }
                                  }

                                  @Override
                                  public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                      permissionToken.continuePermissionRequest();
                                  }


                              }
                ).check();
    }

    public String getUid() {
        return Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
    }

    public String getEmail() {
        return Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
    }
}

