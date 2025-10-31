package com.sp.letspace.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class LandlordSessionViewModel extends ViewModel {

    private final MutableLiveData<LandlordApiResponse.GeneralStats> generalStats = new MutableLiveData<>();
    private final MutableLiveData<List<Property>> properties = new MutableLiveData<>();
    private final MutableLiveData<List<LandlordApiResponse.MonthlyReport>> monthlyReports = new MutableLiveData<>();

    // GENERAL STATS
    public LiveData<LandlordApiResponse.GeneralStats> getGeneralStats() {
        return generalStats;
    }

    public void setGeneralStats(LandlordApiResponse.GeneralStats stats) {
        generalStats.setValue(stats);
    }

    // PROPERTIES (use enhanced Property model)
    public LiveData<List<Property>> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> list) {
        properties.setValue(list);
    }

    // MONTHLY REPORTS
    public LiveData<List<LandlordApiResponse.MonthlyReport>> getMonthlyReports() {
        return monthlyReports;
    }

    public void setMonthlyReports(List<LandlordApiResponse.MonthlyReport> list) {
        monthlyReports.setValue(list);
    }
}



