package com.sp.letspace.models;

import java.util.List;

public class Landlord {
    public int id;
    public String name;
    public String email;
    public List<Property> properties;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<Property> getProperties() { return properties; }
}
