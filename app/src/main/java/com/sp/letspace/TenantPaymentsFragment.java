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

import java.util.ArrayList;



public class TenantPaymentsFragment extends Fragment {

    private RecyclerView recyclerView;
    //private PaymentsAdapter adapter;
    //private List<Payment> paymentList = new ArrayList<>();
    private LandlordSessionViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tenant_payments, container, false);

        recyclerView = view.findViewById(R.id.recyclerPayments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //adapter = new PaymentsAdapter(paymentList);
        //recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(LandlordSessionViewModel.class);

        // TODO: Fetch incoming payments via API

        return view;
    }
}
