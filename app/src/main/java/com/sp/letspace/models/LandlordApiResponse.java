package com.sp.letspace.models;

import java.util.List;

import java.util.List;

public class LandlordApiResponse {
    public User user;
    public List<Property> properties;        // Use the enriched Property model
    public List<MonthlyReport> monthly_reports;
    public GeneralStats general_stats;

    public static class User {
        public int id;
        public String name;
        public String email;
        public String role;
    }

    public static class MonthlyReport {
        public int property_id;
        public String name;
        public int unit_count;
        public double total_rent;
        public double collectable_rent;
        public int vacant_units;
        public double vacancy_rate;
        public double occupancy_rate;
    }

    public static class GeneralStats {
        public int total_properties;
        public int total_tenants;
        public double collectable_rent;
        public double occupancy_rate;
        public String landlord_name;   // optional, for ProfileFragment
        public String landlord_email;
        public String landlord_phone;
    }
}


