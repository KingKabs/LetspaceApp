package com.sp.letspace;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sp.letspace.models.ApiResponse;

import retrofit2.Call;


public class ProfileFragment extends Fragment {
    private TextView tvTenantName, tvTenantEmail, tvIDNo, tvPhoneNumber, tvEmergencyContact, tvRegistrationDate, tvUnitNumber, tvProperty, tvBalance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvTenantName = view.findViewById(R.id.tvTenantName);
        tvTenantEmail = view.findViewById(R.id.tvTenantEmail);
        tvIDNo = view.findViewById(R.id.tvIDNo);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        tvEmergencyContact = view.findViewById(R.id.tvEmergencyContact);
        tvRegistrationDate = view.findViewById(R.id.tvRegistrationDate);
        tvUnitNumber = view.findViewById(R.id.tvUnitNumber);
        tvProperty = view.findViewById(R.id.tvProperty);
        tvBalance = view.findViewById(R.id.tvBalance);

        // 🔹 Observe the shared ViewModel
        SessionViewModel vm = new ViewModelProvider(requireActivity()).get(SessionViewModel.class);
        vm.getProfileData().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                tvTenantName.setText(profile.tenant.fname + " " + profile.tenant.lname);
                tvTenantEmail.setText(profile.user.email);
                tvIDNo.setText(profile.tenant.idno);
                tvPhoneNumber.setText(profile.tenant.phone);
                tvEmergencyContact.setText(profile.tenant.emergency_contact);
                tvRegistrationDate.setText("From: " + profile.tenant.registration_date);
                tvUnitNumber.setText(profile.housing_unit.unit_no != null ? profile.housing_unit.unit_no : "-");
                tvProperty.setText(profile.property.name != null ? profile.property.name : "-");
                tvBalance.setText("KES " + profile.financials.balance);
            }
        });

        return view;
    }


}