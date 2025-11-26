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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sp.letspace.models.TechnicianApiResponse;
import com.sp.letspace.models.TechnicianSessionViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TechnicianDashboardActivity extends AppCompatActivity {

    private TechnicianSessionViewModel viewModel;
    private Activity activity = TechnicianDashboardActivity.this;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_dashboard);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarTechnician);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");

        // ViewModel
        viewModel = new ViewModelProvider(this).get(TechnicianSessionViewModel.class);

        // Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_technician);

        // Load default fragment
        loadFragment(new MaintenanceListFragment(), "📊 Stats");

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            String title = "";

            int itemId = item.getItemId();
            if (itemId == R.id.nav_stats) {
                /*selectedFragment = new TechnicianStatsFragment();
                title = "📊 Stats";*/
                selectedFragment = new MaintenanceListFragment();
                title = "🛠️ Requests";

            } else if (itemId == R.id.nav_requests) {
                selectedFragment = new MaintenanceListFragment();
                title = "🛠️ Requests";

            } /*else if (itemId == R.id.nav_profile) {
                selectedFragment = new TechnicianProfileFragment();
                title = "👤 Profile";

            }*/ else if (itemId == R.id.nav_about) {
                selectedFragment = new AboutFragment();
                title = "ℹ️ About";
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment, title);
                return true;
            }
            return false;
        });

        // Load data from API
        loadTechnicianProfile();

        // Internet check
        if (!Utils.isConnected(activity)) {
            Toast.makeText(activity, "No internet connection. Please check your network.", Toast.LENGTH_LONG).show();
        }
    }


    // ------------------------------------------------------
    // Load Technician Profile (API)
    // ------------------------------------------------------
    private void loadTechnicianProfile() {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        // Show loading dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<TechnicianApiResponse> call = apiService.getTechnicianProfile();

        call.enqueue(new Callback<TechnicianApiResponse>() {
            @Override
            public void onResponse(Call<TechnicianApiResponse> call, Response<TechnicianApiResponse> response) {
                if (progressDialog.isShowing()) progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    TechnicianApiResponse data = response.body();

                    // Fill ViewModel
                    viewModel.setUser(data.user);
                    viewModel.setMaintenanceRequests(data.maintenance_requests);
                    if (data.stats != null) {
                        viewModel.setStats(data.stats);
                    }

                    Log.d("TechDashboard", "Loaded maintenance requests: " +
                            (data.maintenance_requests != null ? data.maintenance_requests.size() : 0));

                } else {
                    Toast.makeText(activity, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TechnicianApiResponse> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                Toast.makeText(activity, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    // ------------------------------------------------------
    // Fragment Loader
    // ------------------------------------------------------
    private void loadFragment(Fragment fragment, String title) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            prefs.edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
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
}
