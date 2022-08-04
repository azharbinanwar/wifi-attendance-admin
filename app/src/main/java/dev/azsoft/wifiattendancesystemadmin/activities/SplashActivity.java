package dev.azsoft.wifiattendancesystemadmin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import dev.azsoft.wifiattendancesystemadmin.Repository;
import dev.azsoft.wifiattendancesystemadmin.activities.user.LoginActivity;
import dev.azsoft.wifiattendancesystemadmin.activities.wifi.AddWifiActivity;
import dev.azsoft.wifiattendancesystemadmin.databasehelper.SharedPrefs;
import dev.azsoft.wifiattendancesystemadmin.databinding.ActivitySplashBinding;
import dev.azsoft.wifiattendancesystemadmin.global.Const;
import dev.azsoft.wifiattendancesystemadmin.interfaces.OnComplete;
import dev.azsoft.wifiattendancesystemadmin.models.AdminUser;

public class SplashActivity extends AppCompatActivity {
    private final Repository repository = Repository.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    // TODO: not going to use this variable because it module is not complete yet

    private SharedPrefs sharedPrefs;
    private Boolean isFirstLaunch = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ActivitySplashBinding.inflate(getLayoutInflater()).getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        init();
//        repository.fetchAllDocuments();
        final FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            navigateTo(new Intent(getApplicationContext(), LoginActivity.class));
        } else {
            repository.fetchUserDetails(user.getUid(), this::onResult);
        }
    }


    void init() {
        SharedPrefs.getInstance().Initialize(getApplicationContext());
        sharedPrefs = SharedPrefs.getInstance();
        isFirstLaunch = sharedPrefs.getBoolean(Const.FIRST_LAUNCH, false);

    }


    private void navigateTo(Intent intent) {
        new Handler().postDelayed(() -> {
            startActivity(intent);
            finish();
        }, 1000);
    }

    private void onResult(Object ob) {
        if (ob instanceof AdminUser) {
            sharedPrefs.setString(Const.CURRENT_USER, ob.toString());
            navigateTo(new Intent(this, HomeActivity.class));
            finish();
        } else {
            navigateTo(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }


}