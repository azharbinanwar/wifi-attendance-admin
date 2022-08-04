package dev.azsoft.wifiattendancesystemadmin.activities.user;

import static dev.azsoft.wifiattendancesystemadmin.global.Const.DEFAULT_DIALOG_WIDTH;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Objects;

import dev.azsoft.wifiattendancesystemadmin.R;
import dev.azsoft.wifiattendancesystemadmin.Repository;
import dev.azsoft.wifiattendancesystemadmin.activities.HomeActivity;
import dev.azsoft.wifiattendancesystemadmin.databasehelper.SharedPrefs;
import dev.azsoft.wifiattendancesystemadmin.databinding.ActivityLoginBinding;
import dev.azsoft.wifiattendancesystemadmin.global.Const;
import dev.azsoft.wifiattendancesystemadmin.interfaces.TextValidator;
import dev.azsoft.wifiattendancesystemadmin.items.ForgotPasswordItem;
import dev.azsoft.wifiattendancesystemadmin.models.AdminUser;
import dev.azsoft.wifiattendancesystemadmin.utils.Utils;
import dev.azsoft.wifiattendancesystemadmin.utils.Validators;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityLoginBinding binding;
    private TextValidator emailValidator;
    private Repository repository = Repository.getInstance();
    private SharedPrefs sharedPrefs = SharedPrefs.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityLoginBinding.inflate(getLayoutInflater())).getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        binding.btnForgotPassword.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        binding.edtEmail.addTextChangedListener(Validators.emailValidator(binding.edtEmail, binding.textInputLayoutEmail));
        binding.edtPassword.addTextChangedListener(Validators.passwordValidator(binding.edtPassword, binding.textInputLayoutPassword));
    }


    private void onLogIn() {
        Utils.onHideKeyboard(this);
        String email = binding.edtEmail.getText().toString();
        String password = binding.edtPassword.getText().toString();
        if (Validators.isValidEmail(email) instanceof Boolean && Validators.isValidPassword(password) instanceof Boolean) {
            binding.btnLogin.setProcessing(true);
            repository.onLogIn(true, email, password, this::onLoginSuccessful);
        }
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

    private void onLoginSuccessful(Object ob) {
        if (ob instanceof AdminUser) {
            sharedPrefs.setString(Const.CURRENT_USER, ob.toString());
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        } else if (ob instanceof Exception) {
            Toast.makeText(this, ((Exception) ob).getMessage(), Toast.LENGTH_SHORT).show();
        }
        binding.btnLogin.setProcessing(false);
    }
}