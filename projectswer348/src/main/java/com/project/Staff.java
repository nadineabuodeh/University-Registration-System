package com.project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Staff extends User{

    //varibale
    private List<String> idList = new ArrayList<>();
    private String id;

    //constructor
    public Staff(String name, String details){
        super(name, details, "Staff");
        this.id = generateUniqueStaffId(name);
    }

    //generate a unique ID for stuff (YEARNAME_count)
    private String generateUniqueStaffId(String name) {
        int year = LocalDate.now().getYear();
        String baseId = year + name.replaceAll("\\s+", "");
        String uniqueId = baseId;
        int counter = 1;

        while (idList.contains(uniqueId)) {
            uniqueId = baseId + "_" + counter++;
        }

        idList.add(uniqueId);
        return uniqueId;
    }

    //getter method
    public String getId() {
        return id;
    }
}
