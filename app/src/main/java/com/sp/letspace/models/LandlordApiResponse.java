package com.sp.letspace.models;

import java.util.List;

import java.util.List;

public class LandlordApiResponse {
    public User user;
    public List<PropertySummary> properties;
    public List<MonthlyReport> monthly_reports;
    public GeneralStats general_stats;

    public User getUser() {
        return user;
    }

    public List<PropertySummary> getProperties() {
        return properties;
    }

    public List<MonthlyReport> getMonthlyReports() {
        return monthly_reports;
    }

    public GeneralStats getGeneralStats() {
        return general_stats;
    }
}
