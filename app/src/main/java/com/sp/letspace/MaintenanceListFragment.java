package com.sp.letspace;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sp.letspace.adapters.MaintenanceRequestAdapter;
import com.sp.letspace.models.ApiResponse;
import com.sp.letspace.models.MaintenanceRequest;

import java.util.ArrayList;
import java.util.List;


public class MaintenanceListFragment extends Fragment {

    private RecyclerView rvRequests;
    private MaintenanceRequestAdapter adapter;
    private List<MaintenanceRequest> requestList = new ArrayList<>();
    private SessionViewModel sessionViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance_list, container, false);

        rvRequests = view.findViewById(R.id.rvRequests);
        rvRequests.setLayoutManager(new LinearLayoutManager(getContext()));

        sessionViewModel = new ViewModelProvider(requireActivity()).get(SessionViewModel.class);

        sessionViewModel.getProfileData().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null && profile.maintenance_requests != null) {
                adapter = new MaintenanceRequestAdapter(profile.maintenance_requests);
                rvRequests.setAdapter(adapter);
            }
        });


        return view;
    }
}
