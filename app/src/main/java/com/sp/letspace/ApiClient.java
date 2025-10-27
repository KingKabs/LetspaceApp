package com.sp.letspace;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://letspace.silverpalaceltd.com/";
    private static Retrofit retrofit;

    public static Retrofit getClient(Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        // Interceptor to add token
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
            String token = prefs.getString("auth_token", null);

            Request.Builder requestBuilder = original.newBuilder()
                    .header("Accept", "application/json");

            if (token != null && !token.isEmpty()) {
                Log.d("API_TOKEN", "Using token: " + token);
                requestBuilder.header("Authorization", "Bearer " + token);
            } else {
                Log.d("API_TOKEN", "No token found in SharedPreferences");
            }

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

}
