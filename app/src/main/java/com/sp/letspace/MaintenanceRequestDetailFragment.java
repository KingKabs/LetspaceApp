package com.sp.letspace;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sp.letspace.adapters.PhotoPagerAdapter;
import com.sp.letspace.models.MaintenanceRequest;

import java.util.List;

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
        // Photos Section
        CardView cardPhotosSection = view.findViewById(R.id.cardPhotosSection);
        LinearLayout photosContainer = view.findViewById(R.id.photosContainer);

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

                if (request.getPhotos() != null && !request.getPhotos().isEmpty()) {
                    Log.d("MaintenancePhotos", "Received photos: " + request.getPhotos().toString());
                    displayPhotos(request.getPhotos(), photosContainer, cardPhotosSection);
                } else {
                    Log.d("MaintenancePhotos", "No photos found for this request.");
                }


            }
        }

        return view;
    }


    private void displayPhotos(List<String> photos, LinearLayout photosContainer, CardView cardPhotosSection) {
        if (photos == null || photos.isEmpty()) {
            cardPhotosSection.setVisibility(View.GONE);
            Log.d("MaintenancePhotos", "No photos to display");
            return;
        }

        cardPhotosSection.setVisibility(View.VISIBLE);
        photosContainer.removeAllViews();

        for (String url : photos) {
            ImageView imageView = new ImageView(getContext());
            int size = (int) getResources().getDimension(R.dimen.photo_thumbnail_size); // or fixed dp value
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.setMargins(8, 8, 8, 8);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.baseline_image_placeholder_24)
                    .error(R.drawable.baseline_error_24)
                    .into(imageView);

            imageView.setOnClickListener(v -> {
                FullScreenPhotoDialog dialog = FullScreenPhotoDialog.newInstance(url);
                dialog.show(getParentFragmentManager(), "fullscreen_photo");
            });

            photosContainer.addView(imageView);

            for (int i = 0; i < photos.size(); i++) {
                String photoUrl = photos.get(i);
                int position = i;
                imageView.setOnClickListener(v -> showFullscreenGallery(photos, position));
            }

        }
    }

    private void showFullscreenGallery(List<String> photoUrls, int startPosition) {
        Dialog dialog = new Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_fullscreen_gallery);

        ViewPager2 viewPager = dialog.findViewById(R.id.viewPagerPhotos);
        ImageButton btnClose = dialog.findViewById(R.id.btnClose);

        PhotoPagerAdapter adapter = new PhotoPagerAdapter(requireContext(), photoUrls);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(startPosition, false);

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


}

