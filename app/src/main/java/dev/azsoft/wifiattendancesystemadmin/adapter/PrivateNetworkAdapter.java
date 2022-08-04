package dev.azsoft.wifiattendancesystemadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.azsoft.wifiattendancesystemadmin.Repository;
import dev.azsoft.wifiattendancesystemadmin.databinding.PrivateNetworkTileBinding;
import dev.azsoft.wifiattendancesystemadmin.models.PrivateNetwork;

public class PrivateNetworkAdapter extends RecyclerView.Adapter<PrivateNetworkAdapter.ViewHolder> {
    final List<PrivateNetwork> privateNetworkList;
    final Repository repository = Repository.getInstance();

    public PrivateNetworkAdapter(List<PrivateNetwork> privateNetworkList) {
        this.privateNetworkList = privateNetworkList;
    }

    @NonNull
    @Override
    public PrivateNetworkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PrivateNetworkAdapter.ViewHolder(PrivateNetworkTileBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull PrivateNetworkAdapter.ViewHolder holder, int position) {
        PrivateNetwork privateNetwork = privateNetworkList.get(position);
        holder.binding.tvWifiName.setText(privateNetwork.getSsid());
        holder.binding.tvWifiId.setText(privateNetwork.getId());
        if (repository.getUid().equals(privateNetwork.getAddedByUID()))
            holder.binding.tvWifiId.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return privateNetworkList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PrivateNetworkTileBinding binding;

        public ViewHolder(@NonNull PrivateNetworkTileBinding b) {
            super(b.getRoot());
            binding = b;

        }
    }
}
