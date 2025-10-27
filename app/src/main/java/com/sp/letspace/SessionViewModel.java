package com.sp.letspace;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sp.letspace.models.ApiResponse;

public class SessionViewModel extends ViewModel {
    private final MutableLiveData<ApiResponse> profileData = new MutableLiveData<>();

    public LiveData<ApiResponse> getProfileData() {
        return profileData;
    }

    public void setProfileData(ApiResponse data) {
        profileData.setValue(data);
    }
}

