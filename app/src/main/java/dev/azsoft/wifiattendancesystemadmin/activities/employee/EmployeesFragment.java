package dev.azsoft.wifiattendancesystemadmin.activities.employee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import dev.azsoft.wifiattendancesystemadmin.R;
import dev.azsoft.wifiattendancesystemadmin.Repository;
import dev.azsoft.wifiattendancesystemadmin.adapter.EmployeesAdapter;
import dev.azsoft.wifiattendancesystemadmin.databinding.FragmentEmployeesBinding;
import dev.azsoft.wifiattendancesystemadmin.global.Const;
import dev.azsoft.wifiattendancesystemadmin.models.Employee;


public class EmployeesFragment extends Fragment {
    private final Repository repository = Repository.getInstance();

    private FragmentEmployeesBinding binding;
    private List<Employee> employeeList = new ArrayList<>();
    private EmployeesAdapter employeesAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = (binding = FragmentEmployeesBinding.inflate(inflater, container, false)).getRoot();
        setHasOptionsMenu(true);
        intiView();
        loadAllEmployees();
        return root;
    }

    void intiView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.employeeRecyclerView.setLayoutManager(linearLayoutManager);
        employeesAdapter = new EmployeesAdapter(getActivity(), employeeList);
        binding.employeeRecyclerView.setAdapter(employeesAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_employee:
                startActivity(new Intent(getActivity(), AddEmployeeActivity.class));
            case R.id.refresh_employee:
                loadAllEmployees();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(getActivity(), "Good day try", Toast.LENGTH_SHORT).show();
//        if (resultCode == RESULT_OK) {
//            String empData = data.getStringExtra(Const.EMPLOYEE);
//            if (!empData.isEmpty()) {
//                Employee employee = Employee.fromString(empData);
//                employeeList.add(employeeList.size(), employee);
//                employeesAdapter.notifyItemChanged(employeeList.size());
//            }
//        }
//    }

    private void loadAllEmployees() {
        repository.fetchAllDocuments(Const.EMPLOYEES, ob -> {
            if (ob instanceof QuerySnapshot) {
                if (!employeeList.isEmpty()) {
                    employeeList = new ArrayList<>();
                }
                for (DocumentSnapshot snapshot : (QuerySnapshot) ob) {
                    employeeList.add(snapshot.toObject(Employee.class));
                }
                if (employeeList.isEmpty()) {
                    binding.placeHolder.getRoot().setVisibility(View.VISIBLE);
                    binding.placeHolder.placeHolderIcon.setImageResource(R.drawable.ic_people);
                    binding.placeHolder.placeHolderTitle.setText("Please press add button to add an employeee");
                }
                employeesAdapter.notifyDataSetChanged();
            } else if (ob instanceof Exception) {
                Toast.makeText(EmployeesFragment.this.getActivity(), ((Exception) ob).getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EmployeesFragment.this.getActivity(), Const.SOME_THING_WENT_WRONG, Toast.LENGTH_SHORT).show();
            }
            binding.progressCircular.setVisibility(View.GONE);
        });
    }

}