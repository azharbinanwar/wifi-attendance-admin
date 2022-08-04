package dev.azsoft.wifiattendancesystemadmin.activities.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import dev.azsoft.wifiattendancesystemadmin.Repository;
import dev.azsoft.wifiattendancesystemadmin.adapter.AttendanceAdapter;
import dev.azsoft.wifiattendancesystemadmin.adapter.EmployeesAdapter;
import dev.azsoft.wifiattendancesystemadmin.databinding.FragmentHomeBinding;
import dev.azsoft.wifiattendancesystemadmin.global.Const;
import dev.azsoft.wifiattendancesystemadmin.interfaces.OnComplete;
import dev.azsoft.wifiattendancesystemadmin.models.Attendance;


public class HomeFragment extends Fragment {
    private final Repository repository = Repository.getInstance();
    private List<Attendance> attendanceList = new ArrayList<>();
    private AttendanceAdapter attendanceAdapter;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (binding = FragmentHomeBinding.inflate(inflater, container, false)).getRoot();
        setHasOptionsMenu(true);
        intiView();
        listenEmployeesAttendance();
        return root;
    }

    private void listenEmployeesAttendance() {
        repository.firestore.collection(Const.ATTENDANCES).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!attendanceList.isEmpty()) {
                    attendanceList = new ArrayList<>();
                }
                for (int i = 0; i < value.size(); i++) {
                    System.out.println("Repository.onEvent " + value.size());
                    DocumentSnapshot snapshot = value.getDocuments().get(i);
                    String id = snapshot.getString("id");
                    String uid = snapshot.getString("uid");
                    long workingMinutes = snapshot.getLong("workingMinutes");
                    String networkName = snapshot.getString("networkName");
                    repository.firestore.collection(Const.EMPLOYEES).document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot snapshot1 = task.getResult();
                            String userName = snapshot1.getString("name");
                            String profilePhoto = snapshot1.getString("profileImage");
                            Attendance attendance = new Attendance(
                                    id,
                                    uid,
                                    workingMinutes,
                                    networkName,
                                    userName,
                                    profilePhoto
                            );
                            attendanceList.add(attendance);
                            attendanceAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }


    void intiView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.attendanceRecyclerView.setLayoutManager(linearLayoutManager);
        attendanceAdapter = new AttendanceAdapter(getActivity(), attendanceList);
        binding.attendanceRecyclerView.setAdapter(attendanceAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}