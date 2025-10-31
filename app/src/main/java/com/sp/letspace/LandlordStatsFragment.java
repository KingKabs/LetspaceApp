package com.sp.letspace;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sp.letspace.models.LandlordSessionViewModel;


public class LandlordStatsFragment extends Fragment {

    private TextView tvTotalProperties, tvTotalTenants, tvCollectableRent, tvOccupancyRate;
    private LandlordSessionViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landlord_stats, container, false);

        tvTotalProperties = view.findViewById(R.id.tvTotalProperties);
        tvTotalTenants = view.findViewById(R.id.tvTotalTenants);
        tvCollectableRent = view.findViewById(R.id.tvCollectableRent);
        tvOccupancyRate = view.findViewById(R.id.tvOccupancyRate);

        viewModel = new ViewModelProvider(requireActivity()).get(LandlordSessionViewModel.class);

        // Observe profile stats
        viewModel.getGeneralStats().observe(getViewLifecycleOwner(), stats -> {
            if (stats != null) {
                tvTotalProperties.setText(String.valueOf(stats.total_properties));
                tvTotalTenants.setText(String.valueOf(stats.total_tenants));
                tvCollectableRent.setText(String.format("KSh %,.0f", stats.collectable_rent));
                tvOccupancyRate.setText(String.format("%.0f%%", stats.occupancy_rate));
            }
        });

        return view;
    }
}
