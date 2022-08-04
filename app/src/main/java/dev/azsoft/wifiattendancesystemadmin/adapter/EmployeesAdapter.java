package dev.azsoft.wifiattendancesystemadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import dev.azsoft.wifiattendancesystemadmin.R;
import dev.azsoft.wifiattendancesystemadmin.databinding.EmployeeTileBinding;
import dev.azsoft.wifiattendancesystemadmin.models.Employee;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.ViewHolder> {
    final Context context;
    final List<Employee> employeeList;

    public EmployeesAdapter(Context context, List<Employee> employeeList) {
        this.employeeList = employeeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(EmployeeTileBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Employee employee = employeeList.get(position);
        Glide.with(context).load(employee.getProfileImage()).centerCrop()
                .placeholder(R.drawable.ic_person_circle).into(holder.binding.profileImage);
        holder.binding.tvUserName.setText(employee.getName());
        holder.binding.tvEmail.setText(employee.getEmail());
        holder.binding.tvDesignation.setText(employee.getDesignation());
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EmployeeTileBinding binding;

        public ViewHolder(@NonNull EmployeeTileBinding b) {
            super(b.getRoot());
            binding = b;

        }
    }
}
