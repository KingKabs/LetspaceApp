package com.sp.letspace;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.sp.letspace.models.LoginResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    Activity activity = LoginActivity.this;
    private EditText etUsername, etPassword;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        if (!Utils.isConnected(activity)) {
            Toast.makeText(activity, "No internet connection. Please check your network.", Toast.LENGTH_LONG).show();
        }

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
        String role = prefs.getString("user_role", "");

        if (isLoggedIn && role != null && !role.isEmpty()) {

            Intent intent;
            if (role.equalsIgnoreCase("tenant")) {
                intent = new Intent(LoginActivity.this, DashboardActivity.class); // tenant dashboard
            } else if (role.equalsIgnoreCase("landlord") || role.equalsIgnoreCase("property_agent")) {
                intent = new Intent(LoginActivity.this, LandlordDashboardActivity.class); // landlord/property agent dashboard
            } else {
                // fallback in case role is missing
                intent = new Intent(LoginActivity.this, LoginActivity.class);
            }

            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            if (!Utils.isConnected(activity)) {
                Toast.makeText(activity, "No internet connection. Please check your network.", Toast.LENGTH_LONG).show();
                return;
            }

            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Logging in...");
            progressDialog.setCancelable(false); // user can't dismiss
            progressDialog.show();

            String email = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = ApiClient.getClient(activity).create(ApiService.class);
            Call<LoginResponse> call = apiService.login(email, password);

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        String token = response.body().getToken();

                        // ✅ Log the full response for debugging
                        Log.d("LOGIN_SUCCESS", "Token: " + response.body().getToken() +
                                ", User: " + response.body().getUser().getName() +
                                " (" + response.body().getUser().getEmail() + ")" +
                                " (" + response.body().getUser().getRole() + ")");

                        String role = response.body().getUser().getRole(); // "tenant" or "landlord"/"property_agent"
                        //Log.d("USER_ROLE", role);

                        LoginResponse loginResponse = response.body();
                        if (loginResponse != null) {
                            Gson gson = new Gson();
                            String json = gson.toJson(loginResponse);
                            Log.d("API_RESPONSE_JSON", json);
                        } else {
                            Log.d("API_RESPONSE_JSON", "Response body is null");
                        }

                        // Save token and logged in status
                        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("auth_token", token);
                        editor.putString("user_role", role);
                        editor.putBoolean("is_logged_in", true); // ✅ mark logged in
                        editor.apply();


                        if ("tenant".equalsIgnoreCase(role)) {
                            // Proceed to DashboardActivity for tenants
                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        } else if ("landlord".equalsIgnoreCase(role) || "property_agent".equalsIgnoreCase(role)) {
                            // Proceed to a LandlordDashboardActivity
                            Intent intent = new Intent(LoginActivity.this, LandlordDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Unknown role. Cannot proceed.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("LOGIN_ERROR", "Server response: " + errorBody);
                        } catch (Exception e) {
                            Log.e("LOGIN_ERROR", "Failed to parse error body", e);
                        }
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }


}