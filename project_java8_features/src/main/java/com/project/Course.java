package com.project;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Course {

    //varibales
    private String name;
    private int credits;
    private Faculty assignedFaculty;
    private List<MeetingSession> meetingSessions;
    private List<Course> prerequisites;
    private Semester semester;

    //constructor
    public Course(String name, Integer credits, Faculty assignedFaculty, List<MeetingSession> meetingSessions, List<Course> prerequisites, Semester semester) {
        this.name = Optional.ofNullable(name).orElse("Unknown Course");
        this.credits = Optional.ofNullable(credits).orElse(3);
        this.assignedFaculty = Optional.ofNullable(assignedFaculty).orElse(new Faculty("0.0", "Unknown Faculty"));
        this.meetingSessions = Optional.ofNullable(meetingSessions).orElseGet(ArrayList::new);
        this.prerequisites = Optional.ofNullable(prerequisites).orElse(Collections.emptyList());
        this.semester = semester;
    }
    
    //getter and setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public Faculty getAssignedFaculty() {
        return assignedFaculty;
    }

    public void setAssignedFaculty(Faculty assignedFaculty) {
        this.assignedFaculty = assignedFaculty;
    }

    public List<MeetingSession> getMeetingSessions() {
        return meetingSessions;
    }

    public void setMeetingSessions(List<MeetingSession> meetingSessions) {
        this.meetingSessions = meetingSessions;
    }

    public List<Course> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<Course> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    // toString method
    @Override
    public String toString() {
        return "Course{" +
               "name='" + name + '\'' +
               ", credits=" + credits +
               ", assignedFaculty=" + assignedFaculty +
               ", meetingSessions=" + meetingSessions +
               '}';
    }

    public String toView(){
         return "Course{" +
               "name='" + name + '\'' +
               ", credits=" + credits +
               '}';
    }

    //class for Meeting Session
    public static class MeetingSession {

        //variable
        private String day;
        private LocalTime startTime;
        private LocalTime endTime;

        //constructor
        public MeetingSession(String day, LocalTime startTime, LocalTime endTime) {
            this.day = day;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        //getter and setter methods
        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalTime startTime) {
            this.startTime = startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalTime endTime) {
            this.endTime = endTime;
        }

        //toString method
        @Override
        public String toString() {
            return day + " " + startTime + " - " + endTime;
        }

        // find a conflict between to meeting Session
        public boolean conflictsWith(MeetingSession other) {
            return this.day.equals(other.day) && 
                   ((this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime)) ||
                    (other.startTime.isBefore(this.endTime) && other.endTime.isAfter(this.startTime)));
        }
    }
}
