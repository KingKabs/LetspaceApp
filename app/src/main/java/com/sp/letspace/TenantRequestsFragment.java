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

import com.sp.letspace.models.LandlordSessionViewModel;
import com.sp.letspace.models.MaintenanceRequest;

import java.util.ArrayList;
import java.util.List;

public class TenantRequestsFragment extends Fragment {

    private RecyclerView recyclerView;
    //private RequestsAdapter adapter;
    private List<MaintenanceRequest> requestList = new ArrayList<>();
    private LandlordSessionViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tenant_requests, container, false);

        recyclerView = view.findViewById(R.id.recyclerRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //adapter = new RequestsAdapter(requestList);
        //recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(LandlordSessionViewModel.class);

        // TODO: Fetch tenant maintenance requests via API

        return view;
    }
}
