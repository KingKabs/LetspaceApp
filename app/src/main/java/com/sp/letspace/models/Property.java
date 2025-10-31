package com.sp.letspace.models;

import java.util.List;

public class Property {
    public int id;
    public String name;
    public int unit_count;
    public double total_rent;
    public double collectable_rent;
    public int vacant_units;
    public double vacancy_rate;
    public double occupancy_rate;

    // NEW FIELDS for landlord dashboard
    public List<Tenant> tenants;          // from Laravel 'tenants' relation
    public List<Expense> expenses;        // from Laravel 'expenses' relation
    public List<HousingUnit> housing_units; // from Laravel 'housingUnits' relation

    // EXISTING GETTERS
    public int getPropertyId() {
        return id;
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


    public static class Expense {
        public int id;
        public String description;
        public double amount;
    }

    public static class HousingUnit {
        public int id;
        public String unit_name;
        public boolean is_vacant;
    }
}

