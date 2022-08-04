package dev.azsoft.wifiattendancesystemadmin.activities.employee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.Date;
import java.util.Objects;

import dev.azsoft.wifiattendancesystemadmin.Repository;
import dev.azsoft.wifiattendancesystemadmin.databinding.ActivityAddEmployeeBinding;
import dev.azsoft.wifiattendancesystemadmin.models.Employee;
import dev.azsoft.wifiattendancesystemadmin.utils.Validators;


public class AddEmployeeActivity extends AppCompatActivity {
    private ActivityAddEmployeeBinding binding;
    final private Repository repository = Repository.getInstance();
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityAddEmployeeBinding.inflate(getLayoutInflater())).getRoot());
        initViews();
        binding.btnCreateEmployeeProfile.setOnClickListener(this::onCreateEmployeeProfile);
    }

    void initViews() {
        requestQueue = Volley.newRequestQueue(this);
        binding.edtEmail.addTextChangedListener(Validators.emailValidator(binding.edtEmail, binding.textInputLayoutEmail));
        binding.edtPassword.addTextChangedListener(Validators.passwordValidator(binding.edtPassword, binding.textInputLayoutPassword));
    }

    private void onCreateEmployeeProfile(View view) {
        String name = Objects.requireNonNull(binding.edtName.getText()).toString();
        String email = Objects.requireNonNull(binding.edtEmail.getText()).toString();
        String password = Objects.requireNonNull(binding.edtPassword.getText()).toString();
        String phoneNumber = Objects.requireNonNull(binding.edtPhoneNumber.getText()).toString();
        String designation = binding.edtDesignation.getText().toString();

        Employee employee = new Employee(null, name,
                phoneNumber, email,
                designation, password,
                new Date().getTime(), null,
                true);


        if (Validators.isValidEmail(email) instanceof Boolean && Validators.isValidPassword(password) instanceof Boolean) {
            binding.btnCreateEmployeeProfile.setProcessing(true);
            repository.onCreateEmployee(employee, requestQueue, ob -> {
                if (ob instanceof VolleyError) {
                    Toast.makeText(getApplicationContext(), ((VolleyError) ob).getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (ob instanceof Exception) {
                    Toast.makeText(getApplicationContext(), ((Exception) ob).getMessage(), Toast.LENGTH_SHORT).show();
                } else if (ob instanceof Employee) {
                    finish();
                }
                binding.btnCreateEmployeeProfile.setProcessing(false);
            });
        }
    }

}