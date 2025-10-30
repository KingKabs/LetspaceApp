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

import com.sp.letspace.adapters.TransactionAdapter;
import com.sp.letspace.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionsFragment extends Fragment {

    private RecyclerView rvTransactions;
    private TransactionAdapter adapter;
    private List<Transaction> transactions = new ArrayList<>();
    private SessionViewModel sessionViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        rvTransactions = view.findViewById(R.id.recyclerTransactions);
        rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));

        sessionViewModel = new ViewModelProvider(requireActivity()).get(SessionViewModel.class);

        sessionViewModel.getTenantProfileData().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null && profile.transactions != null) {
                List<Transaction> transactions = profile.transactions;
                adapter = new TransactionAdapter(getContext(), transactions);
                rvTransactions.setAdapter(adapter);
            }
        });

        return view;
    }


}
