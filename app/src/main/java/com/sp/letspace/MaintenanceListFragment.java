package com.sp.letspace;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sp.letspace.adapters.MaintenanceRequestAdapter;
import com.sp.letspace.models.LandlordSessionViewModel;
import com.sp.letspace.models.MaintenanceRequest;
import com.sp.letspace.models.TechnicianSessionViewModel;

import java.util.ArrayList;
import java.util.List;


public class MaintenanceListFragment extends Fragment {

    private RecyclerView rvRequests;
    private MaintenanceRequestAdapter adapter;
    private List<MaintenanceRequest> requestList = new ArrayList<>();
    private SessionViewModel sessionViewModel;
    private LandlordSessionViewModel landlordSessionViewModel;
    private TechnicianSessionViewModel technicianSessionViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance_list, container, false);

        rvRequests = view.findViewById(R.id.rvRequests);
        rvRequests.setLayoutManager(new LinearLayoutManager(getContext()));

        // --- Read user role from SharedPreferences ---
        SharedPreferences prefs = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String role = prefs.getString("user_role", "");

        if (role.equalsIgnoreCase("tenant")) {
            // Tenant ViewModel (existing logic)
            sessionViewModel = new ViewModelProvider(requireActivity()).get(SessionViewModel.class);
            sessionViewModel.getTenantProfileData().observe(getViewLifecycleOwner(), profile -> {
                if (profile != null && profile.maintenance_requests != null) {
                    adapter = new MaintenanceRequestAdapter(profile.maintenance_requests);
                    rvRequests.setAdapter(adapter);
                }
            });

        } else if (role.equalsIgnoreCase("landlord") || role.equalsIgnoreCase("property_agent")) {
            // Landlord / Property Agent ViewModel
            landlordSessionViewModel = new ViewModelProvider(requireActivity()).get(LandlordSessionViewModel.class);

            landlordSessionViewModel.getMaintenanceRequests().observe(getViewLifecycleOwner(), requests -> {
                if (requests != null && !requests.isEmpty()) {
                    Log.d("LandlordRequestsFragment", "Loaded " + requests.size() + " requests");
                    adapter = new MaintenanceRequestAdapter(requests);
                    rvRequests.setAdapter(adapter);
                } else {
                    Log.d("LandlordRequestsFragment", "No requests found");
                }
            });
        } else if (role.equalsIgnoreCase("technician")) {
            // Technician ViewModel
            technicianSessionViewModel = new ViewModelProvider(requireActivity()).get(TechnicianSessionViewModel.class);
            technicianSessionViewModel.getMaintenanceRequests().observe(getViewLifecycleOwner(), requests -> {
                if (requests != null && !requests.isEmpty()) {
                    Log.d("TechnicianRequestsFragment", "Loaded " + requests.size() + " requests");
                    adapter = new MaintenanceRequestAdapter(requests);
                    rvRequests.setAdapter(adapter);
                } else {
                    Log.d("TechnicianRequestsFragment", "No requests found");
                }
            });
        }


        // Optional toolbar title update with emoji
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity())
                    .getSupportActionBar()
                    .setTitle("🧰 Maintenance Requests");
        }

        return view;
    }
}

