package com.project;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Registrar {

    //variables
    private List<Student> studentRegister;
    private List<Faculty> facultyRegister;
    private List<Semester> semesters;
    private List<Course> courses;

    //constructor
    public Registrar(){
        this.studentRegister = new ArrayList<>();
        this.facultyRegister = new ArrayList<>();
        this.semesters = new ArrayList<>();
        this.courses = new ArrayList<>();
    }

    //create new Student
    public Student createStudent(String name, String details){
        Student student = new Student(name, details);
        studentRegister.add(student);
        return student;
    }

    //create new Faculty
    public Faculty createFaculty(String name, String details){
        Faculty faculty = new Faculty(name, details);
        facultyRegister.add(faculty);
        return faculty;
    }

    //create new Semester
    public Semester createSemester(String name, LocalDate startDate, LocalDate endDate) {
        Semester newSemester = new Semester(name, startDate, endDate);
        
        // Check for date conflicts with existing semesters
        boolean conflictExists = semesters.stream()
                                .anyMatch(existingSemester -> newSemester.overlapsWith(existingSemester));

        if (conflictExists) {
            throw new IllegalArgumentException("Semester dates conflict with an existing semester.");
        }
        semesters.add(newSemester);
        return newSemester;
    }

    //create new Courses
    public Course createCourse(String name, Integer credits, Faculty assignedFaculty, List<Course.MeetingSession> meetingSessions, List<Course> prerequisites, Semester semester) {
        // Check for scheduling conflicts with assigned faculty in the specified semester
        boolean conflictExists = courses.stream()
            .filter(course -> course.getAssignedFaculty().equals(assignedFaculty) && course.getSemester().equals(semester))
            .flatMap(course -> course.getMeetingSessions().stream())
            .anyMatch(existingSession -> 
                meetingSessions.stream().anyMatch(newSession -> existingSession.conflictsWith(newSession))
            );

        if (conflictExists) {
            throw new IllegalArgumentException("Scheduling conflict detected for faculty in " + semester.getName() + ".");
        }

        // Calculate the total hours from meeting sessions
        int totalHours = meetingSessions.stream()
        .mapToInt(session -> {
            Duration duration = Duration.between(session.getStartTime(), session.getEndTime());
            return (int) duration.toHours();
        })
        .sum();

        // Check if the total hours match the credits
        if (totalHours != credits) {
            throw new IllegalArgumentException("Total hours of meeting sessions (" + totalHours + 
                                            ") do not match the course credits (" + credits + ").");
        }

        Course newCourse = new Course(name, credits, assignedFaculty, meetingSessions, prerequisites, semester);
        courses.add(newCourse);
        return newCourse;
    }

    //View Available Courses
    public String browseAllCourses() {
        return courses.stream()
                      .map(course -> course.toString())
                      .collect(Collectors.joining("\n"));
    }    
    
    // View Available Courses for a specific semester
    public String browseAvailableCourses(Semester semester) {
        return courses.stream()
                      .filter(course -> course.getSemester().equals(semester))
                      .map(Course::toString)
                      .collect(Collectors.joining("\n"));
    }

    //View Prerequisites for The course
    public String viewPrerequisites(Course course) {
        return course.getPrerequisites().stream()
                     .map(Course::toView)
                     .collect(Collectors.joining("\n"));
    }    

    //Register Student For Course
    public synchronized void registerStudentForCourse(Student student, Course course) {
        student.enrollInCourse(course);
    }

     // Assign a grade to a student
    public void enterGrade(Student student, Course course, String grade) {
        student.addCourseGrade(course, grade);
    }

     // Generate a report of students' academic standings
    public List<String> generateAcademicStandingReport() {
        return studentRegister.stream()
                       .map(student -> student.getName() + ": " + student.getAcademicStanding())
                       .collect(Collectors.toList());
    }

    //getter and setter methods
    public List<Student> getStudentRegister() {
        return studentRegister;
    }

    public void setStudentRegister(List<Student> studentRegister) {
        this.studentRegister = studentRegister;
    }

    public List<Faculty> getFacultyRegister() {
        return facultyRegister;
    }

    public void setFacultyRegister(List<Faculty> facultyRegister) {
        this.facultyRegister = facultyRegister;
    }

    public List<Semester> getSemesters() {
        return semesters;
    }

    public void setSemesters(List<Semester> semesters) {
        this.semesters = semesters;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }    
}
