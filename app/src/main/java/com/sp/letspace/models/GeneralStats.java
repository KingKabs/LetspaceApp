package com.sp.letspace.models;

public class GeneralStats {
    public int total_properties;
    public int total_tenants;
    public double collectable_rent;
    public double occupancy_rate;

    public int getTotalProperties() {
        return total_properties;
    }

    public int getTotalTenants() {
        return total_tenants;
    }

    public double getCollectableRent() {
        return collectable_rent;
    }

    public double getOccupancyRate() {
        return occupancy_rate;
    }
}

