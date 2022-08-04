package dev.azsoft.wifiattendancesystemadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import dev.azsoft.wifiattendancesystemadmin.R;
import dev.azsoft.wifiattendancesystemadmin.databinding.AttendanceUserTileBinding;
import dev.azsoft.wifiattendancesystemadmin.models.Attendance;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    final Context context;
    final List<Attendance> attendanceList;

    public AttendanceAdapter(Context context, List<Attendance> attendanceList) {
        this.context = context;
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttendanceAdapter.ViewHolder(AttendanceUserTileBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceAdapter.ViewHolder holder, int position) {
        Attendance attendance = attendanceList.get(position);
//        Glide.with(context).load(attendance.getEmployee().getProfileImage()).centerCrop()
//                .placeholder(R.drawable.ic_person_circle).into(holder.binding.profileImage);
        holder.binding.tvUserName.setText(attendance.getUserName());
        holder.binding.tvWorkingTime.setText("Time: " + attendance.getWorkingMinutes() + " minutes");
        holder.binding.tvDesignation.setText(attendance.getNetworkName());
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AttendanceUserTileBinding binding;

        public ViewHolder(@NonNull AttendanceUserTileBinding b) {
            super(b.getRoot());
            binding = b;

        }
    }
}
