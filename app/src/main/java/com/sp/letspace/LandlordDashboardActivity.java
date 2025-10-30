package com.sp.letspace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sp.letspace.models.ApiResponse;
import com.sp.letspace.models.GeneralStats;
import com.sp.letspace.models.LandlordApiResponse;
import com.sp.letspace.models.Property;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandlordDashboardActivity extends AppCompatActivity {

    private TextView tvTotalProperties, tvTotalTenants, tvCollectableRent, tvOccupancyRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landlord_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbarLandlord);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");

        tvTotalProperties = findViewById(R.id.tvTotalProperties);
        tvTotalTenants = findViewById(R.id.tvTotalTenants);
        tvCollectableRent = findViewById(R.id.tvCollectableRent);
        tvOccupancyRate = findViewById(R.id.tvOccupancyRate);

        loadLandlordProfile();
    }

    private void loadLandlordProfile() {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<LandlordApiResponse> call = apiService.getLandlordProfile();

        call.enqueue(new Callback<LandlordApiResponse>() {
            @Override
            public void onResponse(Call<LandlordApiResponse> call, Response<LandlordApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LandlordApiResponse data = response.body();
                    setDashboardStats(data.getGeneralStats());
                } else {
                    Log.e("LandlordProfile", "Failed to load: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<LandlordApiResponse> call, Throwable t) {
                Log.e("LandlordProfile", "Error: " + t.getMessage());
            }
        });
    }

    private void setDashboardStats(GeneralStats stats) {
        tvTotalProperties.setText(String.valueOf(stats.total_properties));
        tvTotalTenants.setText(String.valueOf(stats.total_tenants));
        tvCollectableRent.setText(String.format("KSh %,.0f", stats.collectable_rent));
        tvOccupancyRate.setText(String.format("%.0f%%", stats.occupancy_rate));

        Log.d("Total Properties", "" + stats.total_properties);
    }


}