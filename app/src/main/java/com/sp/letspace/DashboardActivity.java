package com.sp.letspace;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sp.letspace.models.ApiResponse;

import retrofit2.Call;

public class DashboardActivity extends AppCompatActivity {
    Activity activity = DashboardActivity.this;
    private Toolbar toolbar;
    private ApiResponse tenantProfile;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading profile...");
        progressDialog.setCancelable(false);

        // Load tenant profile
        loadTenantProfile();

        // Load default fragment and set default title
        loadFragment(new StatsFragment(), "📈 Stats");

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            String title = "";

            int id = item.getItemId();

            if (id == R.id.nav_stats) {
                selectedFragment = new StatsFragment();
                title = "📈 Stats";
            } else if (id == R.id.nav_maintenance) {
                selectedFragment = new MaintenanceFragment();
                title = "🛠️ Maintenance";
            } else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
                title = "👤 Profile";
            } else if (id == R.id.nav_maintenance_requests) {
                selectedFragment = new MaintenanceListFragment();
                title = "📋 Maintenance Requests";
            } else if (id == R.id.nav_transactions) {   // 👈 NEW
                selectedFragment = new TransactionsFragment();
                title = "💰 Transactions";
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment, title);
                return true;
            }
            return false;
        });


        if (!Utils.isConnected(activity)) {
            Toast.makeText(activity, "No internet connection. Please check your network.", Toast.LENGTH_LONG).show();
        }
    }

    private void loadFragment(Fragment fragment, String title) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    // Inflate the menu into the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    // Handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();  // clears token + is_logged_in
            editor.apply();

            // Redirect to login screen
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();

            return true;
        } else if (id == R.id.action_notices) {
            loadFragment(new NoticesFragment(), "Notices");
            return true;
        } else if (id == R.id.action_about) {
            loadFragment(new AboutFragment(), "About us");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadTenantProfile() {
        progressDialog.show();

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<ApiResponse> call = apiService.getUserProfile();

        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse tenantProfile = response.body();

                    // ✅ Push to ViewModel
                    SessionViewModel vm = new ViewModelProvider(DashboardActivity.this)
                            .get(SessionViewModel.class);
                    vm.setTenantProfileData(tenantProfile);

                    Log.d("Profile", "Loaded tenant profile: " + tenantProfile.user.email);
                } else {
                    Log.e("Profile", "Failed to load profile: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("Profile", "Error: " + t.getMessage());
            }
        });
    }


}
