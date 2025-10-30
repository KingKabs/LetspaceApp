package com.sp.letspace.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    public List<String> photos;

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

    public List<String> getPhotos() {
        if (photos == null) {
            return new ArrayList<>();
        }
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
}

