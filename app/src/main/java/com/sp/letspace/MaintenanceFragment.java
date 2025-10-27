package com.sp.letspace;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sp.letspace.models.ApiResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MaintenanceFragment extends Fragment {

    private EditText etTitle, etIssueDescription;
    private Spinner spCategory;
    private Button btnUploadPhoto, btnSubmit, btnViewRequests;
    private static final int PICK_IMAGE_REQUEST = 100;
    private Uri selectedImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance, container, false);

        etTitle = view.findViewById(R.id.etTitle);
        etIssueDescription = view.findViewById(R.id.etIssueDescription);
        //spCategory = view.findViewById(R.id.spinnerCategory);
        AutoCompleteTextView spCategory = view.findViewById(R.id.spCategory);
        btnUploadPhoto = view.findViewById(R.id.btnUploadPhoto);
        btnSubmit = view.findViewById(R.id.btnSubmitRequest);
        //btnViewRequests = view.findViewById(R.id.btnViewRequests);

        // Populate categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.maintenance_categories, // define in strings.xml
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);
        spCategory.setAdapter(adapter);

        // Handle photo upload
        btnUploadPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Handle submit
        btnSubmit.setOnClickListener(v -> {
            if (!Utils.isConnected(requireContext())) {
                Toast.makeText(getContext(), "No internet connection. Please check your network.", Toast.LENGTH_LONG).show();
                return;
            }

            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Submitting request...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            String title = etTitle.getText().toString().trim();
            //String category = spCategory.getSelectedItem().toString();
            String category = spCategory.getText().toString().trim();
            String description = etIssueDescription.getText().toString().trim();

            if (category.equals("Select Category")) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Please select a category", Toast.LENGTH_SHORT).show();
                return; // stop submission
            }

            // Convert text fields to RequestBody
            RequestBody titlePart = RequestBody.create(MediaType.parse("text/plain"), title);
            RequestBody categoryPart = RequestBody.create(MediaType.parse("text/plain"), category);
            RequestBody descriptionPart = RequestBody.create(MediaType.parse("text/plain"), description);

            // Handle optional photo
            MultipartBody.Part photoPart = null;
            if (selectedImageUri != null) {
                String imagePath = getRealPathFromURI(selectedImageUri);
                File file = new File(imagePath); // path you get from file chooser or camera
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                photoPart = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
            }

            if (title.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
                return;
            }

            if (category.isEmpty() || category.equals("Select Category")) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Please select a category", Toast.LENGTH_SHORT).show();
                return;
            }

            if (description.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Please enter a description", Toast.LENGTH_SHORT).show();
                return;
            }


            ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
            Call<ApiResponse> call = apiService.submitMaintenance(titlePart, categoryPart, descriptionPart, photoPart);

            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(getContext(), "Request submitted!", Toast.LENGTH_SHORT).show();
                        etIssueDescription.setText("");
                        spCategory.setSelection(0);
                    } else {
                        // Debug: log error body
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "null";
                            Log.e("MaintenanceDebug", "Error response: " + errorBody);
                            Toast.makeText(getContext(), "Error: " + errorBody, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e("MaintenanceDebug", "Error parsing errorBody", e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


        /*btnViewRequests.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new MaintenanceListFragment());
            transaction.addToBackStack(null); // so tenant can go back
            transaction.commit();
        });*/


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            // Show preview (optional if you have an ImageView)
            ImageView imagePreview = getView().findViewById(R.id.imagePreview);
            imagePreview.setImageURI(selectedImageUri);
            imagePreview.setVisibility(View.VISIBLE);
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String result;
        Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


}