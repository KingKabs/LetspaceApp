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

import com.sp.letspace.adapters.PropertiesAdapter;
import com.sp.letspace.models.LandlordSessionViewModel;
import com.sp.letspace.models.Property;

import java.util.ArrayList;
import java.util.List;

public class PropertiesFragment extends Fragment {

    private RecyclerView recyclerView;
    private PropertiesAdapter adapter;
    private List<Property> propertyList = new ArrayList<>();
    private LandlordSessionViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_properties, container, false);

        recyclerView = view.findViewById(R.id.recyclerProperties);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PropertiesAdapter(propertyList, property -> {
            // Navigate to TenantsFragment and pass property id
            Bundle bundle = new Bundle();
            bundle.putInt("property_id", property.getPropertyId());
            bundle.putString("property_name", property.getName());

            Fragment tenantsFragment = new TenantsFragment();
            tenantsFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, tenantsFragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerView.setAdapter(adapter);


        // Get the shared ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(LandlordSessionViewModel.class);

        // Observe properties LiveData
        viewModel.getProperties().observe(getViewLifecycleOwner(), properties -> {
            if (properties != null) {
                propertyList.clear();
                propertyList.addAll(properties);
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}

