package com.sp.letspace.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class TechnicianSessionViewModel extends ViewModel {

    private final MutableLiveData<TechnicianApiResponse.User> user = new MutableLiveData<>();
    private final MutableLiveData<List<MaintenanceRequest>> maintenanceRequests = new MutableLiveData<>();
    private final MutableLiveData<TechnicianApiResponse.TechnicianStats> stats = new MutableLiveData<>();

    // Expose LiveData
    public LiveData<TechnicianApiResponse.User> getUser() {
        return user;
    }

    public LiveData<List<MaintenanceRequest>> getMaintenanceRequests() {
        return maintenanceRequests;
    }

    public LiveData<TechnicianApiResponse.TechnicianStats> getStats() {
        return stats;
    }

    // Setter methods
    public void setUser(TechnicianApiResponse.User user) {
        this.user.setValue(user);
    }

    public void setMaintenanceRequests(List<MaintenanceRequest> requests) {
        this.maintenanceRequests.setValue(requests);
    }

    public void setStats(TechnicianApiResponse.TechnicianStats stats) {
        this.stats.setValue(stats);
    }
}


