package com.project;

import java.util.LinkedHashMap;
import java.util.Map;

public class Grade {
    
    //varibales
    private static final Map<String, Double> gradeToGPA = new LinkedHashMap<>();

    static {
        gradeToGPA.put("A", 4.0);
        gradeToGPA.put("B+", 3.5);
        gradeToGPA.put("B", 3.0);
        gradeToGPA.put("C+", 2.5);
        gradeToGPA.put("C", 2.0);
        gradeToGPA.put("D+", 1.5);
        gradeToGPA.put("D", 1.0);
        gradeToGPA.put("F", 0.0);
    }
    
    //to convert grade from char to double
    public static double getGPA(String grade) {
        return gradeToGPA.getOrDefault(grade, 0.0);
    }
}
