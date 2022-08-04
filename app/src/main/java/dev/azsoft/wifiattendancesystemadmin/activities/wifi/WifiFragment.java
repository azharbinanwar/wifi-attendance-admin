package dev.azsoft.wifiattendancesystemadmin.activities.wifi;

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
import dev.azsoft.wifiattendancesystemadmin.adapter.PrivateNetworkAdapter;
import dev.azsoft.wifiattendancesystemadmin.databinding.FragmentWifiBinding;
import dev.azsoft.wifiattendancesystemadmin.global.Const;
import dev.azsoft.wifiattendancesystemadmin.models.PrivateNetwork;


public class WifiFragment extends Fragment {

    private FragmentWifiBinding binding;
    private List<PrivateNetwork> privateNetworks = new ArrayList<>();
    private PrivateNetworkAdapter privateNetworkAdapter;
    private final Repository repository = Repository.getInstance();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = (binding = FragmentWifiBinding.inflate(inflater, container, false)).getRoot();
        setHasOptionsMenu(true);
        intiView();
        loadAllEmployees();

        return root;
    }

    void intiView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.privateNetworksRecyclerView.setLayoutManager(linearLayoutManager);
        privateNetworkAdapter = new PrivateNetworkAdapter(privateNetworks);
        binding.privateNetworksRecyclerView.setAdapter(privateNetworkAdapter);
    }


    private void loadAllEmployees() {
        repository.fetchAllDocuments(Const.PRIVATE_NETWORKS, ob -> {
            if (ob instanceof QuerySnapshot) {
                if (!privateNetworks.isEmpty()) {
                    privateNetworks = new ArrayList<>();
                }

                for (DocumentSnapshot snapshot : (QuerySnapshot) ob) {
                    privateNetworks.add(snapshot.toObject(PrivateNetwork.class));
                }
                if (privateNetworks.isEmpty()) {
                    binding.placeHolder.getRoot().setVisibility(View.VISIBLE);
                    binding.placeHolder.placeHolderIcon.setImageResource(R.drawable.ic_wifi);
                    binding.placeHolder.placeHolderTitle.setText(R.string.please_add_wifi_networks);
                }
                privateNetworkAdapter.notifyDataSetChanged();
            } else if (ob instanceof Exception) {
                Toast.makeText(this.getActivity(), ((Exception) ob).getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.getActivity(), Const.SOME_THING_WENT_WRONG, Toast.LENGTH_SHORT).show();
            }
            binding.progressCircular.setVisibility(View.GONE);
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_wifi:
                startActivity(new Intent(getActivity(), AddWifiActivity.class));
            case R.id.refresh_wifi:
                loadAllEmployees();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}