package com.sp.letspace;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;


public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        // Buttons
        MaterialButton btnRateApp = view.findViewById(R.id.btnRateApp);
        MaterialButton btnPrivacyPolicy = view.findViewById(R.id.btnPrivacyPolicy);
        MaterialButton btnShareApp = view.findViewById(R.id.btnShareApp);

        // ⭐ Rate App → open Play Store
        btnRateApp.setOnClickListener(v -> {
            try {
                Uri uri = Uri.parse("market://details?id=" + requireContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                // Fallback if Play Store not found
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + requireContext().getPackageName());
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });

        // 🔒 Privacy Policy → open web page
        btnPrivacyPolicy.setOnClickListener(v -> {
            String url = "https://www.letspace.com/privacy"; // change to your real policy URL
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        // 📤 Share App → share Play Store link
        btnShareApp.setOnClickListener(v -> {
            String shareText = "Check out this app: https://play.google.com/store/apps/details?id="
                    + requireContext().getPackageName();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Letspace App");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        return view;
    }
}
