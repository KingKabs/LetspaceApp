package com.sp.letspace;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    private Uri cameraImageUri;
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
            if (hasPermissions()) {
                showImageSourceDialog();
            } else {
                requestPermissions();
            }
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

            try {
                // Get real path from URI
                String imagePath = getRealPathFromURI(selectedImageUri);
                if (imagePath == null) {
                    Toast.makeText(requireContext(), "Unable to load image.", Toast.LENGTH_SHORT).show();
                    return;
                }

                File originalFile = new File(imagePath);

                // ✅ Check file size before upload
                double fileSizeInMB = originalFile.length() / (1024.0 * 1024.0);

                if (fileSizeInMB > 2.0) {
                    // Compress large image using helper
                    File compressedFile = compressImage(originalFile);
                    double newFileSize = compressedFile.length() / (1024.0 * 1024.0);

                    selectedImageUri = Uri.fromFile(compressedFile);
                    Toast.makeText(requireContext(),
                            String.format("Image compressed from %.2fMB to %.2fMB", fileSizeInMB, newFileSize),
                            Toast.LENGTH_LONG).show();
                }

                // ✅ Show preview
                ImageView imagePreview = getView().findViewById(R.id.imagePreview);
                imagePreview.setImageURI(selectedImageUri);
                imagePreview.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Error processing image.", Toast.LENGTH_SHORT).show();
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
        String filePath = null;

        // Case 1: Document from file scheme (content:// or file://)
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor = null;
            try {
                String[] projection = {MediaStore.Images.Media.DATA};
                cursor = requireContext().getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    filePath = cursor.getString(columnIndex);
                }
            } catch (Exception e) {
                // Some URIs (esp. on Android 10+) don’t have DATA column
                filePath = copyUriToTempFile(uri);
            } finally {
                if (cursor != null) cursor.close();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }

        return filePath;
    }

    private String copyUriToTempFile(Uri uri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            File tempFile = new File(requireContext().getCacheDir(), "temp_" + System.currentTimeMillis() + ".jpg");
            OutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            return tempFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }


    private void showImageSourceDialog() {
        String[] options = {"Capture from Camera", "Select from Gallery"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Upload Photo")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        openCamera();
                    } else {
                        openGallery();
                    }
                })
                .show();
    }


    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            File photoFile;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(requireContext(), "Error creating image file", Toast.LENGTH_SHORT).show();
                return;
            }

            if (photoFile != null) {
                cameraImageUri = FileProvider.getUriForFile(requireContext(),
                        requireContext().getPackageName() + ".fileprovider",
                        photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
                startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private File compressImage(File originalFile) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2; // Reduce resolution by half (you can adjust)
            Bitmap bitmap = BitmapFactory.decodeFile(originalFile.getAbsolutePath(), options);

            File compressedFile = new File(requireContext().getCacheDir(), "compressed_" + originalFile.getName());
            FileOutputStream out = new FileOutputStream(compressedFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out); // 80% quality
            out.flush();
            out.close();

            return compressedFile;
        } catch (IOException e) {
            e.printStackTrace();
            return originalFile; // fallback
        }
    }


    private boolean hasPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES
            }, PERMISSION_REQUEST_CODE);
        } else {
            requestPermissions(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, PERMISSION_REQUEST_CODE);
        }
    }

}