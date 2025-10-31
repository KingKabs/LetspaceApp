package com.sp.letspace.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class LandlordSessionViewModel extends ViewModel {

    // General stats for landlord dashboard
    private final MutableLiveData<LandlordApiResponse.GeneralStats> generalStats = new MutableLiveData<>();

    // List of landlord properties
    private final MutableLiveData<List<Property>> properties = new MutableLiveData<>();

    // Monthly reports
    private final MutableLiveData<List<LandlordApiResponse.MonthlyReport>> monthlyReports = new MutableLiveData<>();

    // Maintenance requests for all tenants under landlord's properties
    private final MutableLiveData<List<MaintenanceRequest>> maintenanceRequests = new MutableLiveData<>();


    // --- GENERAL STATS ---
    public LiveData<LandlordApiResponse.GeneralStats> getGeneralStats() {
        return generalStats;
    }

    public void setGeneralStats(LandlordApiResponse.GeneralStats stats) {
        generalStats.setValue(stats);
    }


    // --- PROPERTIES ---
    public LiveData<List<Property>> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> list) {
        properties.setValue(list);
    }


    // --- MONTHLY REPORTS ---
    public LiveData<List<LandlordApiResponse.MonthlyReport>> getMonthlyReports() {
        return monthlyReports;
    }

    public void setMonthlyReports(List<LandlordApiResponse.MonthlyReport> list) {
        monthlyReports.setValue(list);
    }


    // --- MAINTENANCE REQUESTS ---
    public LiveData<List<MaintenanceRequest>> getMaintenanceRequests() {
        return maintenanceRequests;
    }

    public void setMaintenanceRequests(List<MaintenanceRequest> list) {
        maintenanceRequests.setValue(list);
    }
}




