package com.sp.letspace;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sp.letspace.models.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    Activity activity = MainActivity.this;
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

        if (isLoggedIn) {
            // Skip login, go straight to dashboard
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            finish();
        }

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //layout animation (background)
        /*ConstraintLayout layout = findViewById(R.id.main);
        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();*/


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
                Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
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
                                " (" + response.body().getUser().getEmail() + ")");

                        // Save token and logged in status
                        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("auth_token", token);
                        editor.putBoolean("is_logged_in", true); // ✅ mark logged in
                        editor.apply();

                        // Go to Dashboard
                        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("LOGIN_ERROR", "Server response: " + errorBody);
                        } catch (Exception e) {
                            Log.e("LOGIN_ERROR", "Failed to parse error body", e);
                        }
                        Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }


}