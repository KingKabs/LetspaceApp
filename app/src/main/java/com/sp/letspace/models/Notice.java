package com.sp.letspace.models;

public class Notice {
    private String title;
    private String message;
    public String created_at;

    // Constructor
    public Notice(String title, String message) {
        this.title = title;
        this.message = message;
    }

    // Empty constructor (needed for Gson/Retrofit parsing)
    public Notice() {
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFormattedDate() {
        if (created_at == null) return "";
        try {
            java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault());
            java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault());
            java.util.Date date = inputFormat.parse(created_at);
            return outputFormat.format(date);
        } catch (Exception e) {
            return created_at; // fallback
        }
    }
}

