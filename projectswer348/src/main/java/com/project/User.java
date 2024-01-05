package com.project;

import java.util.Optional;

public class User {

    //variables
    private String name;
    private String contactDetails;
    private String role;

    //constructor
    public User(String name, String contactDetails, String role) {
        this.name = Optional.ofNullable(name).orElseThrow(() -> new IllegalArgumentException("Name cannot be null"));
        this.contactDetails = Optional.ofNullable(contactDetails).orElse("no-contact details");
        this.role = Optional.ofNullable(role).orElse("user");
    }

    //getter and setter methods
    public String getName() {
        return name;
    }

    public String getContactDetails() {
        return contactDetails;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setContactDetails(String contactDetails) {
        this.contactDetails = Optional.ofNullable(contactDetails).orElse("0");
    }

    public void setRole(String role) {
        this.role = Optional.ofNullable(role).orElse("user");
    }

    //toString method
    @Override
    public String toString() {
        return "UserProfile{" +
               "name='" + name + '\'' +
               ", contactDetails='" + contactDetails + '\'' +
               ", role='" + role + '\'' +
               '}';
    }  
}
