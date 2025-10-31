package com.sp.letspace;

import android.app.Activity;
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
import com.sp.letspace.models.LandlordApiResponse;
import com.sp.letspace.models.LandlordSessionViewModel;
import com.sp.letspace.models.Property;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LandlordDashboardActivity extends AppCompatActivity {

    private LandlordSessionViewModel viewModel;
    private Activity activity = LandlordDashboardActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landlord_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbarLandlord);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");

        viewModel = new ViewModelProvider(this).get(LandlordSessionViewModel.class);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_landlord);

        // Load default fragment
        loadFragment(new LandlordStatsFragment(), "📈 Stats");

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            String title = "";

            int itemId = item.getItemId();
            if (itemId == R.id.nav_stats) {
                selectedFragment = new LandlordStatsFragment();
                title = "📈 Stats";
            } else if (itemId == R.id.nav_properties) {
                selectedFragment = new PropertiesFragment();
                title = "🏠 Properties";
            } /*else if (itemId == R.id.nav_requests) {
                selectedFragment = new TenantRequestsFragment();
                title = "🛠️ Requests";
            } else if (itemId == R.id.nav_payments) {
                selectedFragment = new TenantPaymentsFragment();
                title = "💰 Payments";
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
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

        // Load landlord profile
        loadLandlordProfile();

        // Connectivity check
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

    private void loadLandlordProfile() {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<LandlordApiResponse> call = apiService.getLandlordProfile();

        call.enqueue(new Callback<LandlordApiResponse>() {
            @Override
            public void onResponse(Call<LandlordApiResponse> call, Response<LandlordApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LandlordApiResponse data = response.body();

                    // 1️⃣ Merge monthly_reports into properties
                    for (Property prop : data.properties) {
                        for (LandlordApiResponse.MonthlyReport report : data.monthly_reports) {
                            if (report.property_id == prop.getPropertyId()) {
                                prop.unit_count = report.unit_count;
                                prop.total_rent = report.total_rent;
                                prop.collectable_rent = report.collectable_rent;
                                prop.vacant_units = report.vacant_units;
                                prop.vacancy_rate = report.vacancy_rate;
                                prop.occupancy_rate = report.occupancy_rate;
                                break;
                            }
                        }
                    }

                    // 2️⃣ Fill ViewModel
                    LandlordSessionViewModel vm = new ViewModelProvider(LandlordDashboardActivity.this)
                            .get(LandlordSessionViewModel.class);

                    // General stats
                    LandlordApiResponse.GeneralStats stats = data.general_stats;
                    stats.landlord_name = data.user.name;
                    stats.landlord_email = data.user.email;
                    vm.setGeneralStats(stats);

                    // Properties
                    vm.setProperties(data.properties);

                    // Monthly Reports (optional, can still keep for separate fragment)
                    vm.setMonthlyReports(data.monthly_reports);

                } else {
                    Log.e("LandlordProfile", "Failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<LandlordApiResponse> call, Throwable t) {
                Log.e("LandlordProfile", "Error: " + t.getMessage());
            }
        });
    }


}
