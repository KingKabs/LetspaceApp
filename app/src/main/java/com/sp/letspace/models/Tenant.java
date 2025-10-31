package com.sp.letspace.models;

public class Tenant {
    public int id;
    public String fname;
    public String lname;
    public String idno;
    public String phone;
    public String emergency_contact;
    public String registration_date;
    public Double deposit_amount;
    public int property_id;
    public int housing_unit_id;
    public Property property;
    public HousingUnit housing_unit;

    public String getFullName() {
        String first = fname != null ? fname : "";
        String last = lname != null ? lname : "";
        String full = (first + " " + last).trim();
        return full.isEmpty() ? "Unnamed Tenant" : full;
    }

    public String getDisplayUnit() {
        return housing_unit != null && housing_unit.unit_no != null
                ? housing_unit.unit_no
                : "Unit ID: " + housing_unit_id;
    }

    public String getDisplayProperty() {
        return property != null && property.name != null
                ? property.name
                : "Property ID: " + property_id;
    }
}
