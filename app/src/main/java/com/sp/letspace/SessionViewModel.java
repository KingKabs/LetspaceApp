package com.sp.letspace;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sp.letspace.models.ApiResponse;
import com.sp.letspace.models.LandlordApiResponse;

public class SessionViewModel extends ViewModel {
    private final MutableLiveData<ApiResponse> tenantProfileData = new MutableLiveData<>();
    private MutableLiveData<LandlordApiResponse> landlordProfileData = new MutableLiveData<>();

    public LiveData<ApiResponse> getTenantProfileData() {
        return tenantProfileData;
    }

    public void setTenantProfileData(ApiResponse data) {
        tenantProfileData.setValue(data);
    }

    public void setLandlordProfile(LandlordApiResponse profile) {
        landlordProfileData.setValue(profile);
    }

    public LiveData<LandlordApiResponse> getLandlordProfile() {
        return landlordProfileData;
    }
}

