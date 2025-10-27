package com.sp.letspace.models;

import java.io.Serializable;

public class MaintenanceRequest implements Serializable {

    public int id;
    public String title;
    public String description;
    public String category;
    public String priority;
    public String request_date;
    public String status;
    public String summary;
    public Technician technician;

    public MaintenanceRequest(String category, String description, String status) {
        this.category = category;
        this.description = description;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getSummary() {
        return summary;
    }
}

