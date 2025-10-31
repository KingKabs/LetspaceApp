package com.sp.letspace;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sp.letspace.adapters.TenantsAdapter;
import com.sp.letspace.models.LandlordSessionViewModel;
import com.sp.letspace.models.Property;
import com.sp.letspace.models.Tenant;

import java.util.ArrayList;
import java.util.List;

public class TenantsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TenantsAdapter adapter;
    private List<Tenant> tenantList = new ArrayList<>();
    private LandlordSessionViewModel viewModel;

    private int propertyId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tenants, container, false);

        recyclerView = view.findViewById(R.id.recyclerTenants);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TenantsAdapter(tenantList);
        recyclerView.setAdapter(adapter);

        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("🏡 Property Tenants");

        // Get property id from bundle
        if (getArguments() != null) {
            propertyId = getArguments().getInt("property_id", -1);
        }

        // Get tenants from ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(LandlordSessionViewModel.class);
        viewModel.getProperties().observe(getViewLifecycleOwner(), properties -> {
            tenantList.clear();
            if (properties != null) {
                for (Property prop : properties) {
                    if (prop.getPropertyId() == propertyId) {
                        if (prop.tenants != null) {
                            tenantList.addAll(prop.tenants);
                        }
                        break;
                    }
                }
            }
            adapter.notifyDataSetChanged();
        });

        return view;
    }
}
