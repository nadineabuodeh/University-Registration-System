package com.project;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class Semester {

    //varibles
    private static final Set<String> VALID_SEMESTER_NAMES = new LinkedHashSet<>(Arrays.asList("Fall", "Spring", "Summer"));
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int year;

    //constructor
    public Semester(String name, LocalDate startDate, LocalDate endDate) {
        this.name = validateSemesterName(name);
        this.startDate = Optional.ofNullable(startDate).orElse(LocalDate.now());
        this.endDate = Optional.ofNullable(endDate).orElse(this.startDate.plus(4, ChronoUnit.MONTHS));
        if (this.endDate.isBefore(this.startDate)) {
            this.endDate = this.startDate.plus(4, ChronoUnit.MONTHS);
        }
        this.year = startDate.getYear();
    }

    //to check if semester name is Fall, or Spring, or Summer
    private String validateSemesterName(String name) {
        return VALID_SEMESTER_NAMES.contains(name) ? name : "INVALID";
    }

    // getter methods
    public String getName() {
        return name;
    }
    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getYear() {
        return year;
    }

    //to see if there is a conflict between two semester in the time
    public boolean overlapsWith(Semester other) {
        return !startDate.isAfter(other.endDate) && !endDate.isBefore(other.startDate);
    }

    //toString method
    @Override
    public String toString() {
        return "Semester{" +
               "name='" + name + '\'' +
               ", startDate=" + startDate +
               ", endDate=" + endDate +
               ", year=" + year +
               '}';
    }
}
