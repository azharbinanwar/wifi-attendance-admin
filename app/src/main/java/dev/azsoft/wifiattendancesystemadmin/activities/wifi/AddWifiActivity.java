package dev.azsoft.wifiattendancesystemadmin.activities.wifi;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import java.util.Date;

import dev.azsoft.wifiattendancesystemadmin.R;
import dev.azsoft.wifiattendancesystemadmin.Repository;
import dev.azsoft.wifiattendancesystemadmin.databinding.ActivityAddWifiBinding;
import dev.azsoft.wifiattendancesystemadmin.global.Const;
import dev.azsoft.wifiattendancesystemadmin.global.PermissionStatus;
import dev.azsoft.wifiattendancesystemadmin.interfaces.OnComplete;
import dev.azsoft.wifiattendancesystemadmin.models.PrivateNetwork;

public class AddWifiActivity extends AppCompatActivity {
    private static final String TAG = "AddWifiActivity";
    private ActivityAddWifiBinding binding;
    private final Repository repository = Repository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityAddWifiBinding.inflate(getLayoutInflater())).getRoot());
        repository.onPermissionHandler(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION, new OnComplete() {
            @Override
            public void onResult(Object ob) {
                if (ob instanceof PermissionStatus) {
                    if (ob == PermissionStatus.permissionGranted) {
                        showWifiDetails();
                    } else if (ob == PermissionStatus.permissionDeniedPermanently) {
                        binding.openSettings.setVisibility(View.VISIBLE);
                        binding.progressCircular.setVisibility(View.GONE);
                        binding.tvPermissionDenied.setVisibility(View.GONE);
                        binding.openSettings.setOnClickListener(view -> showSettingsDialog());
                    } else if (ob == PermissionStatus.permissionDenied) {
                        binding.tvPermissionDenied.setVisibility(View.VISIBLE);
                        binding.openSettings.setVisibility(View.GONE);
                        binding.progressCircular.setVisibility(View.GONE);
                        Toast.makeText(AddWifiActivity.this, R.string.you_denied_permission, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddWifiActivity.this, Const.SOME_THING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    void showWifiDetails() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiManager.isWifiEnabled()) {
            System.out.println("AddWifiActivity.showWifiDetails: " + wifiManager.isWifiEnabled());
            Log.d(TAG, "getBSSID: " + wifiInfo.getBSSID());
            Log.d(TAG, "getNetworkId: " + wifiInfo.getNetworkId());
            Log.d(TAG, "getIpAddress: " + wifiInfo.getIpAddress());
            Log.d(TAG, "getSSID: " + wifiInfo.getSSID());
            Log.d(TAG, "onCreate: MAC " + wifiInfo.getMacAddress());

            String ssid = wifiInfo.getSSID();
            String bssid = wifiInfo.getBSSID();
            int networkId = wifiInfo.getNetworkId();
            int ipAddress = wifiInfo.getIpAddress();
            String macAddress = wifiInfo.getMacAddress();
            binding.placeHolder.getRoot().setVisibility(View.GONE);
            binding.tvWifiName.setText(ssid);
            binding.tvWifiBssid.setText(bssid);
            binding.tvWifiNetworkId.setText(String.valueOf(networkId));
            binding.tvWifiMacAddress.setText(macAddress);
            binding.tvWifiNetworkIp.setText(String.valueOf(ipAddress));
            binding.btnAddWifi.setOnClickListener(view -> {
                PrivateNetwork privateNetwork = new PrivateNetwork(bssid, ssid, bssid, networkId, ipAddress, macAddress, new Date().getTime(), repository.getUid());
                onAddWifi(privateNetwork);
            });
        } else {
            binding.wifiDetails.setVisibility(View.GONE);
            binding.placeHolder.getRoot().setVisibility(View.VISIBLE);
            binding.placeHolder.placeHolderIcon.setImageResource(R.drawable.ic_wifi);
            binding.placeHolder.placeHolderTitle.setText(R.string.please_connect_to_the_wifi);
            binding.placeHolder.placeHolderSubtitle.setText(R.string.you_re_not_connected_to_any_wifi);
        }
    }

    void onAddWifi(PrivateNetwork privateNetwork) {
        repository.onCreateDocument(privateNetwork.getId(), privateNetwork, Const.PRIVATE_NETWORKS, ob -> {
            if (ob instanceof PrivateNetwork) {
                Toast.makeText(this, "Add Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddWifiActivity.this, ((Exception) ob).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Need Permissions");

        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}