package com.sp.letspace;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.sp.letspace.R;

public class FullScreenPhotoDialog extends DialogFragment {

    private static final String ARG_PHOTO_URL = "photo_url";

    public static FullScreenPhotoDialog newInstance(String photoUrl) {
        FullScreenPhotoDialog fragment = new FullScreenPhotoDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PHOTO_URL, photoUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fullscreen_photo, null);
        dialog.setContentView(view);

        ImageView fullscreenImage = view.findViewById(R.id.fullscreenImage);
        ImageButton btnClose = dialog.findViewById(R.id.btnClose);
        String photoUrl = getArguments() != null ? getArguments().getString(ARG_PHOTO_URL) : null;

        if (photoUrl != null) {
            Glide.with(requireContext())
                    .load(photoUrl)
                    .into(fullscreenImage);

            // Load image with Glide
            Glide.with(requireContext())
                    .load(photoUrl)
                    .placeholder(R.drawable.baseline_image_placeholder_24)
                    .into(fullscreenImage);

            // Close button dismisses the dialog
            btnClose.setOnClickListener(v -> dialog.dismiss());
        }

        view.setOnClickListener(v -> dismiss()); // tap anywhere to close
        return dialog;
    }
}
