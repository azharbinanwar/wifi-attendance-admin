package dev.azsoft.wifiattendancesystemadmin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Objects;

import dev.azsoft.wifiattendancesystemadmin.activities.user.LoginActivity;
import dev.azsoft.wifiattendancesystemadmin.databinding.ActivitySplashBinding;
import dev.azsoft.wifiattendancesystemadmin.global.Const;
import dev.azsoft.wifiattendancesystemadmin.utils.SharedPrefs;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ActivitySplashBinding.inflate(getLayoutInflater()).getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        SharedPrefs.getInstance().Initialize(getApplicationContext());
        SharedPrefs prefs = SharedPrefs.getInstance();

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, prefs.getBoolean(Const.FIRST_LAUNCH, false) ? LoginActivity.class : LoginActivity.class));
            finish();
        }, 000);
    }
}