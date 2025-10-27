package com.sp.letspace;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    private static final int CAMERA_REQUEST = 2;
    private static final int PICK_IMAGE_REQUEST = 1001;
    private static final int CAPTURE_IMAGE_REQUEST = 1002;
    private static final int PERMISSION_REQUEST_CODE = 2001;

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
            if (!hasPermissions()) {
                requestPermissionsSafe();
                return;
            }

            String[] options = {"Take Photo", "Choose from Gallery"};
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Select Photo")
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            // Take photo from camera
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (cameraIntent.resolveActivity(requireContext().getPackageManager()) != null) {
                                File photoFile = null;
                                try {
                                    photoFile = createImageFile();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }

                                if (photoFile != null) {
                                    Uri photoURI = FileProvider.getUriForFile(requireContext(),
                                            requireContext().getPackageName() + ".fileprovider",
                                            photoFile);
                                    selectedImageUri = photoURI;
                                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                }
                            }
                        } else {
                            // Choose from gallery
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            galleryIntent.setType("image/*");
                            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
                        }
                    })
                    .show();
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

        if (resultCode == Activity.RESULT_OK) {
            ImageView imagePreview = getView().findViewById(R.id.imagePreview);

            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                selectedImageUri = data.getData();

            } else if (requestCode == CAMERA_REQUEST) {
                // selectedImageUri already set when photo was captured
            }

            if (selectedImageUri != null) {
                imagePreview.setImageURI(selectedImageUri);
                imagePreview.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Permissions granted. You can now upload a photo.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Camera and storage permissions are required to upload photos.", Toast.LENGTH_LONG).show();
            }
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

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }

    private boolean hasPermissions() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionsSafe() {
        requestPermissions(new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, PERMISSION_REQUEST_CODE);
    }
}