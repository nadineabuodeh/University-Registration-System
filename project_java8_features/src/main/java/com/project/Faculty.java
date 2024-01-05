package com.project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Faculty extends User{

    //variables
    private List<String> idList = new ArrayList<>();
    private String id;

    //constructor        
    public Faculty(String name, String details){
        super(name,details, "Faculty");
        this.id = generateUniqueFacultyId(name);
    }

    //getter method
    public String getId() {
        return id;
    }

    //enter grade for student
    public void enterGrade(Student student, Course course, String grade) throws UnauthorizedFacultyException {
        if (course.getAssignedFaculty().equals(this)) {
            student.addCourseGrade(course, grade);
        } else {
            throw new UnauthorizedFacultyException("Faculty member is not authorized to enter grade for this course.");
        }
    }
    
    //generate a ID for the faculty (YEARNAME_COUNT)
    private String generateUniqueFacultyId(String name) {
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
}
