package dev.azsoft.wifiattendancesystemadmin.activities.user;

import static dev.azsoft.wifiattendancesystemadmin.global.Const.DEFAULT_DIALOG_WIDTH;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import dev.azsoft.wifiattendancesystemadmin.MainActivity;
import dev.azsoft.wifiattendancesystemadmin.R;
import dev.azsoft.wifiattendancesystemadmin.databinding.ActivityLoginBinding;
import dev.azsoft.wifiattendancesystemadmin.items.ForgotPasswordItem;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding;
        setContentView((binding = ActivityLoginBinding.inflate(getLayoutInflater())).getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        binding.btnForgotPassword.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
    }


    private void onLogIn() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    private void onForgotPassword() {
        new ForgotPasswordItem(DEFAULT_DIALOG_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT).show(getSupportFragmentManager(), ForgotPasswordItem.TAG);
    }

    @Override
    public void onClick(View view) {
        int clickedId = view.getId();
        if (clickedId == R.id.btn_forgot_password) onForgotPassword();
        else if (clickedId == R.id.btn_login) onLogIn();
    }
}