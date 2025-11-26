package com.sp.letspace.models;

import java.util.List;

public class TechnicianApiResponse {

    public User user;
    public List<MaintenanceRequest> maintenance_requests;
    public TechnicianStats stats;

    // ===== Inner Classes =====
    public static class User {
        public int id;
        public String name;
        public String email;
        public String phone;
        public String role; // "technician"
        public String specialization;
    }

    public static class TechnicianStats {
        public int total_requests;
        public int pending_requests;
        public int completed_requests;
    }
}
