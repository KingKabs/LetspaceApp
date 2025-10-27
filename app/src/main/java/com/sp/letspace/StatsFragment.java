package com.sp.letspace;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.sp.letspace.models.ApiResponse;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class StatsFragment extends Fragment {
    private TextView tvRegistrationDate, tvUnitNumber, tvRentDeposit, tvBalance, tvMaintenanceSummary, tvNotices;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment_stats.xml layout
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        tvRegistrationDate = view.findViewById(R.id.tvRegistrationDate);
        tvUnitNumber = view.findViewById(R.id.tvUnitNumber);
        tvRentDeposit = view.findViewById(R.id.tvRentDeposit);
        tvBalance = view.findViewById(R.id.tvBalance);
        tvMaintenanceSummary = view.findViewById(R.id.tvMaintenanceSummary);
        tvNotices = view.findViewById(R.id.tvNotices);

        MaterialCardView cardMaintenance = view.findViewById(R.id.cardMaintenance);
        MaterialCardView cardNotices = view.findViewById(R.id.cardNotices);
        MaterialCardView cardBilling = view.findViewById(R.id.cardBilling);


        // Load MaintenanceListFragment
        cardMaintenance.setOnClickListener(v -> {
            Fragment fragment = new MaintenanceListFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Load NoticesFragment
        cardNotices.setOnClickListener(v -> {
            Fragment fragment = new NoticesFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Load TransactionsFragment
        cardBilling.setOnClickListener(v -> {
            Fragment fragment = new TransactionsFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        // 🔹 Observe shared SessionViewModel
        SessionViewModel vm = new ViewModelProvider(requireActivity()).get(SessionViewModel.class);
        vm.getProfileData().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                // Create a currency formatter for KES
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "KE"));
                currencyFormatter.setCurrency(Currency.getInstance("KES"));

                tvRegistrationDate.setText("From: " + profile.tenant.registration_date);
                tvUnitNumber.setText("Unit: Apartment " +
                        (profile.housing_unit.unit_no != null ? profile.housing_unit.unit_no : "-"));

                // Deposit
                String depositFormatted = currencyFormatter.format(profile.tenant.deposit_amount);
                tvRentDeposit.setText(depositFormatted);

                // Balance
                String balanceFormatted = currencyFormatter.format(profile.financials.balance);
                tvBalance.setText("Outstanding Balance: " + balanceFormatted);

                tvMaintenanceSummary.setText(profile.maintenance_summary.summary);

                tvNotices.setText("" + profile.notices.size());
            }
        });

        return view;
    }


}
