package com.sp.letspace;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sp.letspace.models.MaintenanceRequest;

public class MaintenanceRequestDetailFragment extends Fragment {

    private TextView title, description, category, priority, date, status, tvTechnicianHeader, technicianName, technicianPhone, technicianEmail, technicianSpecialization;
    CardView technicianSection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance_request_detail, container, false);

        title = view.findViewById(R.id.tvTitle);
        description = view.findViewById(R.id.tvDescription);
        category = view.findViewById(R.id.tvCategory);
        priority = view.findViewById(R.id.tvPriority);
        date = view.findViewById(R.id.tvRequestDate);
        status = view.findViewById(R.id.tvStatus);

        tvTechnicianHeader = view.findViewById(R.id.tvTechnicianHeader);
        technicianName = view.findViewById(R.id.tvTechnicianName);
        technicianPhone = view.findViewById(R.id.tvTechnicianPhone);
        technicianEmail = view.findViewById(R.id.tvTechnicianEmail);
        technicianSpecialization = view.findViewById(R.id.tvTechnicianSpecialization);
        technicianSection = view.findViewById(R.id.cardTechnicianSection);

        if (getArguments() != null) {
            MaintenanceRequest request = (MaintenanceRequest) getArguments().getSerializable("request");
            if (request != null) {
                title.setText(request.title);
                description.setText(request.description);
                category.setText(request.category);
                priority.setText(request.priority);
                date.setText(request.request_date);
                status.setText(request.status);

                if (request.technician != null) {
                    technicianSection.setVisibility(View.VISIBLE);
                    technicianName.setText(request.technician.name);
                    technicianPhone.setText(request.technician.phone);
                    technicianEmail.setText(request.technician.email);
                    technicianSpecialization.setText(request.technician.specialization);

                    technicianSection.setOnClickListener(v -> {
                        String phoneNumber = technicianPhone.getText().toString().trim();
                        if (!phoneNumber.isEmpty()) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + phoneNumber));
                            startActivity(intent);
                        } else {
                            Toast.makeText(requireContext(), "Phone number not available", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    technicianSection.setVisibility(View.VISIBLE);

                    technicianName.setText("👷 Technician not yet assigned");
                    technicianName.setTextColor(Color.parseColor("#757575")); // Grey color
                    technicianName.setTypeface(null, Typeface.ITALIC); // Italic style

                    tvTechnicianHeader.setVisibility(View.GONE);
                    technicianPhone.setVisibility(View.GONE);
                    technicianEmail.setVisibility(View.GONE);
                    technicianSpecialization.setVisibility(View.GONE);
                }

            }
        }

        return view;
    }

}

