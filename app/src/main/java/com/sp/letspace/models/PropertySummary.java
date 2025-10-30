package com.sp.letspace.models;

public class PropertySummary {
    public int property_id;
    public String name;
    public int unit_count;
    public double total_rent;
    public double collectable_rent;
    public int vacant_units;
    public double vacancy_rate;
    public double occupancy_rate;

    public int getPropertyId() {
        return property_id;
    }

    public String getName() {
        return name;
    }

    public int getUnitCount() {
        return unit_count;
    }

    public double getTotalRent() {
        return total_rent;
    }

    public double getCollectableRent() {
        return collectable_rent;
    }

    public int getVacantUnits() {
        return vacant_units;
    }

    public double getVacancyRate() {
        return vacancy_rate;
    }

    public double getOccupancyRate() {
        return occupancy_rate;
    }
}

