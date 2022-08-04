package dev.azsoft.wifiattendancesystemadmin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import dev.azsoft.wifiattendancesystemadmin.R;
import dev.azsoft.wifiattendancesystemadmin.Repository;
import dev.azsoft.wifiattendancesystemadmin.activities.user.LoginActivity;
import dev.azsoft.wifiattendancesystemadmin.databasehelper.SharedPrefs;
import dev.azsoft.wifiattendancesystemadmin.databinding.ActivityHomeBinding;
import dev.azsoft.wifiattendancesystemadmin.databinding.NavHeaderHomeBinding;
import dev.azsoft.wifiattendancesystemadmin.global.Const;
import dev.azsoft.wifiattendancesystemadmin.models.AdminUser;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    final private SharedPrefs sharedPrefs = SharedPrefs.getInstance();
    final private Repository repository = Repository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityHomeBinding.inflate(getLayoutInflater())).getRoot());
        setSupportActionBar(binding.appBarHome.toolbar);
        initView();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_employees, R.id.nav_wifi)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void initView() {
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        setUserDetails();
    }


    private void setUserDetails() {
        String currentUser = sharedPrefs.getString(Const.CURRENT_USER);
        if (!currentUser.isEmpty()) {
            NavHeaderHomeBinding navHeaderHomeBinding = NavHeaderHomeBinding.bind(navigationView.getHeaderView(0));
            AdminUser adminUser = AdminUser.fromString(currentUser);
            Glide.with(getApplicationContext()).load(adminUser.getProfileImage()).centerCrop()
                    .placeholder(R.drawable.app_icon).into(navHeaderHomeBinding.profileImage);
            navHeaderHomeBinding.tvUserEmail.setText(adminUser.getEmail());
            navHeaderHomeBinding.tvUserName.setText(adminUser.getName());
        } else {
            repository.signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("HomeActivity.onCreateOptionsMenu nav_home " + navigationView.getMenu().findItem(R.id.nav_home).isChecked());
        System.out.println("HomeActivity.onCreateOptionsMenu nav_employees " + navigationView.getMenu().findItem(R.id.nav_employees).isChecked());
        if (navigationView.getMenu().findItem(R.id.nav_home).isChecked()) {
            getMenuInflater().inflate(R.menu.menu_home, menu);
            return true;
        } else if (navigationView.getMenu().findItem(R.id.nav_employees).isChecked()) {
            getMenuInflater().inflate(R.menu.emplyees_menu, menu);
            return true;
        } else if (navigationView.getMenu().findItem(R.id.nav_wifi).isChecked()) {
            getMenuInflater().inflate(R.menu.wifi_menu, menu);
            return true;
        } else
            return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
               repository.signOut();
               startActivity(new Intent(getApplicationContext(), LoginActivity.class));
               finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}