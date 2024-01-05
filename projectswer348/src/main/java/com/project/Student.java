package com.project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class Student extends User{

    //variables
    private static final Random random = new Random();
    private List<String> idList = new ArrayList<>();
    private String id;
    private List<Course> enrolledCourses;
    private Map<Course, String> courseGrades;
    private Map<Semester, Double> semesterGPAs;

    //constructor
    public Student(String name, String contact){
        super(name, contact, "student");
        this.id = generateUniqueStudentId();
        this.enrolledCourses = new ArrayList<>();
        this.courseGrades = new LinkedHashMap<>();
        semesterGPAs = new LinkedHashMap<>();
    }
    
    //getter and stter method
    public String getId() {
        return id;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public Map<Course, String> getCourseGrades() {
        return courseGrades;
    }

    public void setCourseGrades(Map<Course, String> courseGrades) {
        this.courseGrades = courseGrades;
    }

    public Map<Semester, Double> getSemesterGPAs() {
        return semesterGPAs;
    }

    public void setSemesterGPAs(Map<Semester, Double> semesterGPAs) {
        this.semesterGPAs = semesterGPAs;
    }

    //generate a Unique ID student (YEAR0(RANDOM NUMBER HAVE 4 DIGITS))
    private String generateUniqueStudentId() {
        int year = LocalDate.now().getYear();
        String id;
        do {
            int randomDigits = 1000 + random.nextInt(9000); // Generate a 4-digit number
            id = year + "0" + randomDigits;
        } while (idList.contains(id));
        idList.add(id);
        return id;
    }

    //enroll student to course
    public synchronized void enrollInCourse(Course course) {
        // Check if the student is already enrolled in the course
        if (enrolledCourses.contains(course)) {
            throw new IllegalStateException("Already enrolled in the course.");
        }

        //check the prerequisites courses
        List<Course> prerequisites = course.getPrerequisites();
        boolean hasCompletedPrerequisites = prerequisites.stream()
            .allMatch(prerequisite -> 
                enrolledCourses.stream()
                    .anyMatch(enrolled -> enrolled.equals(prerequisite) && !courseGrades.get(enrolled).equals("F"))
            );
        if (!hasCompletedPrerequisites) {
            throw new IllegalArgumentException("Prerequisite courses not completed.");
        }

        //check the schedule of student in the same semester
        for (Course.MeetingSession session : course.getMeetingSessions()) {
            boolean hasConflict = enrolledCourses.stream()
                .filter(enrolledCourse -> enrolledCourse.getSemester().equals(course.getSemester()))
                .flatMap(enrolledCourse -> enrolledCourse.getMeetingSessions().stream())
                .anyMatch(enrolledSession -> enrolledSession.conflictsWith(session));
            if (hasConflict) {
                throw new IllegalArgumentException("Scheduling conflict detected.");
            }
        }
        enrolledCourses.add(course);
    }

    //add Grade for course
    public synchronized void addCourseGrade(Course course, String grade) {
        // Check if the student is enrolled in the course
        if (!enrolledCourses.contains(course)) {
            throw new IllegalStateException("Cannot add grade. Student not enrolled in the course.");
        }
        // Add the grade for the course
        courseGrades.put(course, grade);
    }

    //Calculate CUM. EARNED GPA
    public double calculateCumEarnedGPA() {
        double totalWeightedGrades = courseGrades.entrySet().stream()
            .mapToDouble(entry -> Grade.getGPA(entry.getValue()) * entry.getKey().getCredits())
            .sum();

        int totalCredits = courseGrades.entrySet().stream()
            .mapToInt(entry -> entry.getKey().getCredits())
            .sum();

        return totalCredits > 0 ? totalWeightedGrades / totalCredits : 0;
    }

    // Calculate GPA for a specific semester
    public double calculateGPASpecificSemester(Semester semester) {
        double totalWeightedGrades = courseGrades.entrySet().stream()
            .filter(entry -> entry.getKey().getSemester().equals(semester))
            .mapToDouble(entry -> Grade.getGPA(entry.getValue()) * entry.getKey().getCredits())
            .sum();

        int totalCredits = courseGrades.entrySet().stream()
            .filter(entry -> entry.getKey().getSemester().equals(semester))
            .mapToInt(entry -> entry.getKey().getCredits())
            .sum();

        return totalCredits > 0 ? totalWeightedGrades / totalCredits : 0;
    }

    //specify the Academic Standing for student
    public String getAcademicStanding() {
        double gpa = calculateCumEarnedGPA();
        if (gpa >= 3.5) {
            return "Highest Honours";
        } else if (gpa >= 3.0) {
            return "Honours";
        } else if (gpa < 1.9) {
            return "Probation";
        }
        return "Regular Standing";
    }

    //Calculate GPA for each semester for student
    public String calculateSemesterGPAs() {
        List<Semester> semesters = courseGrades.keySet().stream()
            .map(Course::getSemester)
            .collect(Collectors.toList());
    
        Map<Semester, Double> semesterGPAValues = semesters.stream()
            .collect(Collectors.toMap(
                semester -> semester, 
                this::calculateGPASpecificSemester)
            );
    
        return semesterGPAValues.entrySet().stream()
            .map(entry -> entry.getKey().toString() + ": " + entry.getValue())
            .collect(Collectors.joining("\n"));
    }    

    // Generate academic standing report
    public String generateAcademicStandingReport() {
        List<Semester> semesters = courseGrades.keySet().stream()
            .map(Course::getSemester)
            .collect(Collectors.toList());
    
        Map<Semester, Double> semesterGPAValues = semesters.stream()
            .collect(Collectors.toMap(
                semester -> semester, 
                this::calculateGPASpecificSemester)
            );
    
        return semesterGPAValues.entrySet().stream()
            .map(entry -> entry.getKey().toString() + ": " + determineAcademicStanding(entry.getValue()))
            .collect(Collectors.joining("\n"));
    }
    
    //give an academic standing according to a specific gpa
    private String determineAcademicStanding(double gpa) {
        if (gpa >= 3.5) {
            return "Highest Honours";
        } else if (gpa >= 3.0) {
            return "Honours";
        } else if (gpa < 1.9) {
            return "Probation";
        }
        return "Regular Standing";
    }

    //parallel 
    public double calculateGPAs() {
        List<Entry<Course, String>> courseGradeEntries = new ArrayList<>(courseGrades.entrySet());
        RecursiveTask<Double> task = new GPACalculationTask(courseGradeEntries, 0, courseGradeEntries.size());
        try (ForkJoinPool pool = new ForkJoinPool()) {
            return pool.invoke(task);
        }
    }

    private static class GPACalculationTask extends RecursiveTask<Double> {
        private final static int THRESHOLD = 10;
        private List<Entry<Course, String>> list;
        private int low;
        private int high;

        public GPACalculationTask(List<Entry<Course, String>> list, int low, int high) {
            this.list = list;
            this.low = low;
            this.high = high;
        }

        @Override
        protected Double compute() {
            if (high - low <= THRESHOLD) {
                double sum = 0;
                int totalCredits = 0;
                for (int i = low; i < high; i++) {
                    Entry<Course, String> entry = list.get(i);
                    sum += Grade.getGPA(entry.getValue()) * entry.getKey().getCredits();
                    totalCredits += entry.getKey().getCredits();
                }
                return totalCredits > 0 ? sum / totalCredits : 0;
            } else {
                int mid = (low + high) / 2;
                GPACalculationTask left = new GPACalculationTask(list, low, mid);
                GPACalculationTask right = new GPACalculationTask(list, mid, high);
                right.fork();
                left.compute();
                return right.join();
            }
        }
    }
}