package com.sp.letspace;

import com.sp.letspace.models.ApiResponse;
import com.sp.letspace.models.LandlordApiResponse;
import com.sp.letspace.models.LoginResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @GET("api/user/profile")
    Call<ApiResponse> getUserProfile();

    @GET("api/user/profile")
    Call<LandlordApiResponse> getLandlordProfile();

    @FormUrlEncoded
    @POST("api/login")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @Multipart
    @POST("api/maintenance-requests")
    Call<ApiResponse> submitMaintenance(
            @Part("title") RequestBody title,
            @Part("category") RequestBody category,
            @Part("description") RequestBody description,
            @Part List<MultipartBody.Part> photos
    );

}
