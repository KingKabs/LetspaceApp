package com.sp.letspace.models;

import java.util.List;

public class ApiResponse {
    public User user;
    public Tenant tenant;
    public Property property;
    public HousingUnit housing_unit;
    public Financials financials;
    public List<MaintenanceRequest> maintenance_requests;
    public MaintenanceSummary maintenance_summary;
    public List<Transaction> transactions;
    public List<Notice> notices;
}
