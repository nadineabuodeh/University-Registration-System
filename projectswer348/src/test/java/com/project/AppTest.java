package com.project;

import org.junit.jupiter.api.Test;

import com.project.Course.MeetingSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class AppTest {
    Registrar registrar = new Registrar();
    List<Student> student = registrar.getStudentRegister();
    List<Faculty> faculties = registrar.getFacultyRegister();
    List<Course> courses = registrar.getCourses();
    List<Semester> semesters = registrar.getSemesters();
    User user;

    /*
     * Test - 1
     */
    @Test
    void testCreateUser() throws FileNotFoundException {
         File file = new File("src/test/resources/Test1.txt");
		Scanner sc = new Scanner(file);
		while (sc.hasNextLine()) {
			String[] array = sc.nextLine().split(";");
            user = new User(array[1],array[2],array[3]);
		}
		sc.close();
        assertNotNull(user);
        assertEquals("Maria", user.getName());
        assertEquals("123456789", user.getContactDetails());
        assertEquals("admin", user.getRole());
    }

    /*
     * Test - 2
     */
    @Test
    public void testCreateStudentByRegistrar() throws FileNotFoundException{
        File file = new File("src/test/resources/Test2.txt");
		Scanner sc = new Scanner(file);
		while (sc.hasNextLine()) {
			String[] array = sc.nextLine().split(";");
            registrar.createStudent(array[1], array[2]);
		}
		sc.close();
        assertNotNull(student.get(0), "Student creation failed");
        assertEquals("noor", student.get(0).getName());
        assertEquals("noor@example.com", student.get(0).getContactDetails());
        assertEquals("student", student.get(0).getRole());

        assertNotNull(student.get(1), "Student creation failed");
        assertEquals("nadine", student.get(1).getName());
        assertEquals("nadine@example.com", student.get(1).getContactDetails());
        assertEquals("student", student.get(1).getRole());

        assertNotNull(student.get(2), "Student creation failed");
        assertEquals("salma", student.get(2).getName());
        assertEquals("salma@example.com", student.get(2).getContactDetails());
        assertEquals("student", student.get(2).getRole());
    }

    /*
     * Test - 3
     */
    @Test
    public void testCreateFacultyByRegistrar() throws FileNotFoundException{
        File file = new File("src/test/resources/Test3.txt");
		Scanner sc = new Scanner(file);
		while (sc.hasNextLine()) {
			String[] array = sc.nextLine().split(";");
            registrar.createFaculty(array[1], array[2]);
		}
		sc.close();
        assertNotNull(faculties.get(0), "Faculty creation failed");
        assertEquals("Dr. Iman", faculties.get(0).getName());
        assertEquals("iman@example.com", faculties.get(0).getContactDetails());
        assertEquals("Faculty", faculties.get(0).getRole());

        assertNotNull(faculties.get(1), "Faculty creation failed");
        assertEquals("Mr. Toulin", faculties.get(1).getName());
        assertEquals("toulin@example.com", faculties.get(1).getContactDetails());
        assertEquals("Faculty", faculties.get(1).getRole());

        assertNotNull(faculties.get(2), "Faculty creation failed");
        assertEquals("Dr. Ahmad", faculties.get(2).getName());
        assertEquals("ahmad@example.com", faculties.get(2).getContactDetails());
        assertEquals("Faculty", faculties.get(2).getRole());
    }

    /*
     * Test - 4
     */
    @Test
    void testCreateSemesterFromScanner() throws FileNotFoundException{
        File file = new File("src/test/resources/Test4.txt");
		Scanner sc = new Scanner(file);
		while (sc.hasNextLine()) {
			String[] array = sc.nextLine().split(";");
            registrar.createSemester(array[1], LocalDate.parse(array[2]), LocalDate.parse(array[3]));
		}
		sc.close();
        assertNotNull(semesters.get(0));
        assertEquals("Fall", semesters.get(0).getName());
        assertEquals(LocalDate.of(2023, 9, 1), semesters.get(0).getStartDate());
        assertEquals(LocalDate.of(2023, 12, 15), semesters.get(0).getEndDate());

        assertNotNull(semesters.get(1));
        assertEquals("Spring", semesters.get(1).getName());
        assertEquals(LocalDate.of(2024, 2, 3), semesters.get(1).getStartDate());
        assertEquals(LocalDate.of(2024, 6, 25), semesters.get(1).getEndDate());
    }

    /*
     * Test - 5
     */
    Faculty assignedFaculty1;
    @Test
    void testCreateCourseFromRegitrarWithoutConflict() throws FileNotFoundException {
        File file = new File("src/test/resources/Test5.txt");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String[] array = sc.nextLine().split(";");
            String courseName = array[0];
            int credits = Integer.parseInt(array[1]);
            assignedFaculty1 = registrar.createFaculty(array[2], array[3]);
            boolean isDigit = false;
            for(int i =0; i<array[2].length();i++){
                if (Character.isDigit(array[2].charAt(i))) {
                assignedFaculty1 = faculties.get(Integer.parseInt(array[2]));
                isDigit = true;
                }
            }
            List<Course.MeetingSession> meetingSessions = parseMeetingSessions(array[4]);
            Semester semester = registrar.createSemester(array[6], LocalDate.parse(array[7]), LocalDate.parse(array[8]));
            List<Course> pre = new ArrayList<>();
            if(!array[5].equals("null")){
            pre.add(courses.get(Integer.parseInt(array[5])));
            }
            
            registrar.createCourse(courseName, credits, assignedFaculty1, meetingSessions, pre, semester);
        }
        sc.close();
        assertNotNull(courses.get(0));
        assertEquals("Software Engineering", courses.get(0).getName());
        assertEquals(4, courses.get(0).getCredits());
        assertEquals("Dr. Iman", courses.get(0).getAssignedFaculty().getName());
        assertEquals("iman@example.com", courses.get(0).getAssignedFaculty().getContactDetails());
        assertEquals("Monday", courses.get(0).getMeetingSessions().get(0).getDay());
        assertEquals("09:00", courses.get(0).getMeetingSessions().get(0).getStartTime().toString());
        assertEquals("11:00", courses.get(0).getMeetingSessions().get(0).getEndTime().toString());
        assertEquals("Tuesday", courses.get(0).getMeetingSessions().get(1).getDay());
        assertEquals("11:00", courses.get(0).getMeetingSessions().get(1).getStartTime().toString());
        assertEquals("13:00", courses.get(0).getMeetingSessions().get(1).getEndTime().toString());
        assertEquals(new ArrayList<>(), courses.get(0).getPrerequisites());
        assertEquals("Fall", courses.get(0).getSemester().getName());
        assertEquals("2023-09-01", courses.get(0).getSemester().getStartDate().toString());
        assertEquals("2023-12-15", courses.get(0).getSemester().getEndDate().toString());

        //2
        assertNotNull(courses.get(1));
        assertEquals("Database Systems", courses.get(1).getName());
        assertEquals(3, courses.get(1).getCredits());
        assertEquals("Dr. Iman", courses.get(1).getAssignedFaculty().getName());
        assertEquals("iman@example.com", courses.get(1).getAssignedFaculty().getContactDetails());
        assertEquals("Wednesday", courses.get(1).getMeetingSessions().get(0).getDay());
        assertEquals("14:00", courses.get(1).getMeetingSessions().get(0).getStartTime().toString());
        assertEquals("17:00", courses.get(1).getMeetingSessions().get(0).getEndTime().toString());
        assertEquals("Software Engineering", courses.get(1).getPrerequisites().get(0).getName());
        assertEquals(4, courses.get(1).getPrerequisites().get(0).getCredits());
        assertEquals("Spring", courses.get(1).getSemester().getName());
        assertEquals("2024-02-03", courses.get(1).getSemester().getStartDate().toString());
        assertEquals("2024-06-25", courses.get(1).getSemester().getEndDate().toString());
    }

    //Parse Meeting Sessions from Day fromTime-toTime
    private List<Course.MeetingSession> parseMeetingSessions(String sessionData) {
        List<MeetingSession> meetingSessions = new ArrayList<>();
        String[] sessions = sessionData.split(",");
    
        for (String session : sessions) {
            String[] parts = session.trim().split(" ");
            String day = parts[0];
            String[] times = parts[1].split("-");
            LocalTime startTime = LocalTime.parse(times[0]);
            LocalTime endTime = LocalTime.parse(times[1]);
    
            meetingSessions.add(new MeetingSession(day, startTime, endTime));
        }
    
        return meetingSessions;
    }

    /*
     * Test - 6
     */
    @Test
    void testConstructorWithNullValues() {
        Course course = new Course(null, null, null, null, null, null);
        assertEquals("Unknown Course", course.getName());
        assertEquals(3, course.getCredits());
    }

    /*
     * Test - 7
     */
    Faculty assignedFaculty;
    Semester semester;
    @Test
    void testCreateCourseFromScannerWithConflictForFaculty() throws FileNotFoundException {
        File file = new File("src/test/resources/Test7.txt");
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            String[] array = sc.nextLine().split(";");
            String courseName = array[0];
            int credits = Integer.parseInt(array[1]);
            assignedFaculty = registrar.createFaculty(array[2], array[3]);
            faculties.add(assignedFaculty);
            boolean isDigit = false;
            for(int i =0; i<array[2].length();i++){
                if (Character.isDigit(array[2].charAt(i))) {
                assignedFaculty = faculties.get(Integer.parseInt(array[2]));
                isDigit = true;
                }
            }
            List<Course.MeetingSession> meetingSessions = parseMeetingSessions(array[4]);
            boolean isDigitSem = false;
            for(int i =0; i<array[6].length();i++){
                if (Character.isDigit(array[6].charAt(i))) {
                semester = semesters.get(Integer.parseInt(array[6]));
                isDigitSem = true;
                }
            }
            if (!isDigitSem) {
                semester = registrar.createSemester(array[6], LocalDate.parse(array[7]), LocalDate.parse(array[8]));
                semesters.add(semester);
            }
            List<Course> pre = new ArrayList<>();
            if(!array[5].equals("null")){
            pre.add(courses.get(Integer.parseInt(array[5])));
            }
            if (!isDigit) {
                registrar.createCourse(courseName, credits, assignedFaculty, meetingSessions, pre, semester);
            }
            else{
                IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> registrar.createCourse(courseName, credits, assignedFaculty, meetingSessions, pre, semester));
                assertTrue(thrown.getMessage().contains("Scheduling conflict detected for faculty in " + semesters.get(Integer.parseInt(array[6])).getName() + "."));
            }
        }
        sc.close();
        assertNotNull(courses.get(0));
        assertEquals("Software Engineering", courses.get(0).getName());
        assertEquals(4, courses.get(0).getCredits());
        assertEquals("Dr. Iman", courses.get(0).getAssignedFaculty().getName());
        assertEquals("iman@example.com", courses.get(0).getAssignedFaculty().getContactDetails());
        assertEquals("Monday", courses.get(0).getMeetingSessions().get(0).getDay());
        assertEquals("09:00", courses.get(0).getMeetingSessions().get(0).getStartTime().toString());
        assertEquals("11:00", courses.get(0).getMeetingSessions().get(0).getEndTime().toString());
        assertEquals("Tuesday", courses.get(0).getMeetingSessions().get(1).getDay());
        assertEquals("11:00", courses.get(0).getMeetingSessions().get(1).getStartTime().toString());
        assertEquals("13:00", courses.get(0).getMeetingSessions().get(1).getEndTime().toString());
        assertEquals(new ArrayList<>(), courses.get(0).getPrerequisites());
        assertEquals("Fall", courses.get(0).getSemester().getName());
        assertEquals("2023-09-01", courses.get(0).getSemester().getStartDate().toString());
        assertEquals("2023-12-15", courses.get(0).getSemester().getEndDate().toString());
    }

    /*
     * Test - 8
     */
    @Test
    void checkBrowseAllCourses() throws FileNotFoundException{
        File file = new File("src/test/resources/Test8.txt");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String[] array = sc.nextLine().split(";");
            String courseName = array[0];
            int credits = Integer.parseInt(array[1]);
            Faculty assignedFaculty = registrar.createFaculty(array[2], array[3]);
            List<Course.MeetingSession> meetingSessions = parseMeetingSessions(array[4]);
            Semester semester = registrar.createSemester(array[6], LocalDate.parse(array[7]), LocalDate.parse(array[8]));
            List<Course> pre = new ArrayList<>();
            if(!array[5].equals("null")){
            pre.add(courses.get(Integer.parseInt(array[5])));
            }
            registrar.createCourse(courseName, credits, assignedFaculty, meetingSessions, pre, semester);
        }
        sc.close();
        assertEquals("Course{name='Software Engineering', credits=4, assignedFaculty=UserProfile{name='Dr. Iman', contactDetails='iman@example.com', role='Faculty'}, meetingSessions=[Monday 09:00 - 11:00, Tuesday 11:00 - 13:00]}", registrar.browseAllCourses());
    }

    /*
     * Test - 9
     */
    Faculty assignedFaculty4;
    @Test
    void CheckPrerequisites() throws FileNotFoundException{
        File file = new File("src/test/resources/Test9.txt");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String[] array = sc.nextLine().split(";");
            String courseName = array[0];
            int credits = Integer.parseInt(array[1]);
            assignedFaculty4 = registrar.createFaculty(array[2], array[3]);
            faculties.add(assignedFaculty4);
            boolean isDigit = false;
            for(int i =0; i<array[2].length();i++){
                if (Character.isDigit(array[2].charAt(i))) {
                assignedFaculty4 = faculties.get(Integer.parseInt(array[2]));
                isDigit = true;
                }
            }
            List<Course.MeetingSession> meetingSessions = parseMeetingSessions(array[4]);
            Semester semester = registrar.createSemester(array[6], LocalDate.parse(array[7]), LocalDate.parse(array[8]));
            List<Course> pre = new ArrayList<>();
            if(!array[5].equals("null")){
            pre.add(courses.get(Integer.parseInt(array[5])));
            }
            registrar.createCourse(courseName, credits, assignedFaculty4, meetingSessions, pre, semester);
        }
        sc.close();
        assertEquals("Course{name='Software Engineering', credits=4}", registrar.viewPrerequisites(courses.get(1)));
        assertEquals("", registrar.viewPrerequisites(courses.get(0)));
    }

    /*
     * Test - 10
     */
    @Test
    void checkEnrollStudent() throws FileNotFoundException{
        File file = new File("src/test/resources/Test10.txt");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String[] array = sc.nextLine().split(";");
            if(array[0].equals("Course")){
                String courseName = array[1];
                int credits = Integer.parseInt(array[2]);
                Faculty assignedFaculty = registrar.createFaculty(array[3], array[4]);
                List<Course.MeetingSession> meetingSessions = parseMeetingSessions(array[5]);
                Semester semester = registrar.createSemester(array[7], LocalDate.parse(array[8]), LocalDate.parse(array[9]));
                List<Course> pre = new ArrayList<>();
                if(!array[6].equals("null")){
                    pre.add(courses.get(Integer.parseInt(array[6])));
                }
                registrar.createCourse(courseName, credits, assignedFaculty, meetingSessions, pre, semester);
            }
            else if(array[0].equals("Student")){
                registrar.createStudent(array[1], array[2]);
            }
        }
        sc.close();

        registrar.registerStudentForCourse(student.get(0), courses.get(0));
        assertEquals("Software Engineering", student.get(0).getEnrolledCourses().get(0).getName());
    }

    /*
     * Test - 11
     */
     Faculty assignedFaculty5;
    @Test
    void checkEnrollStudentWithConflictPre() throws FileNotFoundException{
        File file = new File("src/test/resources/Test11.txt");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String[] array = sc.nextLine().split(";");
            if(array[0].equals("Course")){
                String courseName = array[1];
                int credits = Integer.parseInt(array[2]);
                assignedFaculty5 = registrar.createFaculty(array[3], array[4]);
                faculties.add(assignedFaculty5);
                boolean isDigit = false;
                for(int i =0; i<array[3].length();i++){
                    if (Character.isDigit(array[3].charAt(i))) {
                    assignedFaculty5 = faculties.get(Integer.parseInt(array[3]));
                    isDigit = true;
                    }
                }
                List<Course.MeetingSession> meetingSessions = parseMeetingSessions(array[5]);
                Semester semester = registrar.createSemester(array[7], LocalDate.parse(array[8]), LocalDate.parse(array[9]));
                List<Course> pre = new ArrayList<>();
                if(!array[6].equals("null")){
                    pre.add(courses.get(Integer.parseInt(array[6])));
                }
                registrar.createCourse(courseName, credits, assignedFaculty5, meetingSessions, pre, semester);
            }
            else if(array[0].equals("Student")){
                registrar.createStudent(array[1], array[2]);
            }
        }
        sc.close();
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> registrar.registerStudentForCourse(student.get(0), courses.get(1)));
                assertTrue(thrown.getMessage().contains("Prerequisite courses not completed."));
    }

    /*
     * Test - 12
     */
    Semester semester2;
     Faculty assignedFaculty12;
    @Test
    void checkEnrollStudentWithConflictTime() throws FileNotFoundException{
        File file = new File("src/test/resources/Test12.txt");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String[] array = sc.nextLine().split(";");
            if(array[0].equals("Course")){
                String courseName = array[1];
                int credits = Integer.parseInt(array[2]);
                assignedFaculty12 = registrar.createFaculty(array[3], array[4]);
                faculties.add(assignedFaculty12);
                boolean isDigit = false;
                for(int i =0; i<array[3].length();i++){
                    if (Character.isDigit(array[3].charAt(i))) {
                    assignedFaculty12 = faculties.get(Integer.parseInt(array[3]));
                    isDigit = true;
                    }
                }
                List<Course.MeetingSession> meetingSessions = parseMeetingSessions(array[5]);
                boolean isDigitSem = false;
                for(int i =0; i<array[7].length();i++){
                    if (Character.isDigit(array[7].charAt(i))) {
                    semester2 = semesters.get(Integer.parseInt(array[7]));
                    isDigitSem = true;
                    }
                }
                if (!isDigitSem) {
                    semester2 = registrar.createSemester(array[7], LocalDate.parse(array[8]), LocalDate.parse(array[9]));
                    semesters.add(semester2);
                }
                List<Course> pre = new ArrayList<>();
                if(!array[6].equals("null")){
                    pre.add(courses.get(Integer.parseInt(array[6])));
                }
                registrar.createCourse(courseName, credits, assignedFaculty12, meetingSessions, pre, semester2);
            }
            else if(array[0].equals("Student")){
                registrar.createStudent(array[1], array[2]);
            }
        }
        sc.close();
        registrar.registerStudentForCourse(student.get(0), courses.get(0));
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> registrar.registerStudentForCourse(student.get(0), courses.get(1)));
        assertTrue(thrown.getMessage().contains("Scheduling conflict detected."));
    }

    /*
     * Test - 13
     */
    Faculty assignedFaculty13;
    @Test
    void enterGradeForCources() throws FileNotFoundException{
        File file = new File("src/test/resources/Test13.txt");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String[] array = sc.nextLine().split(";");
            if(array[0].equals("Course")){
                String courseName = array[1];
                int credits = Integer.parseInt(array[2]);
                assignedFaculty13 = registrar.createFaculty(array[3], array[4]);
                faculties.add(assignedFaculty13);
                boolean isDigit = false;
                for(int i =0; i<array[3].length();i++){
                    if (Character.isDigit(array[3].charAt(i))) {
                    assignedFaculty13 = faculties.get(Integer.parseInt(array[3]));
                    isDigit = true;
                    }
                }
                List<Course.MeetingSession> meetingSessions = parseMeetingSessions(array[5]);
                Semester semester = registrar.createSemester(array[7], LocalDate.parse(array[8]), LocalDate.parse(array[9]));
                List<Course> pre = new ArrayList<>();
                if(!array[6].equals("null")){
                    pre.add(courses.get(Integer.parseInt(array[6])));
                }
                registrar.createCourse(courseName, credits, assignedFaculty13, meetingSessions, pre, semester);
            }
            else if(array[0].equals("Student")){
                registrar.createStudent(array[1], array[2]);
            }
        }
        sc.close();

        registrar.registerStudentForCourse(student.get(0), courses.get(0));
        registrar.registerStudentForCourse(student.get(0), courses.get(1));
        registrar.enterGrade(student.get(0), courses.get(0), "B+");
        registrar.enterGrade(student.get(0), courses.get(1), "B");
        assertEquals(3.2857142857142856, student.get(0).calculateCumEarnedGPA());
        assertEquals(3.5, student.get(0).calculateGPASpecificSemester(student.get(0).getEnrolledCourses().get(0).getSemester()));
        assertEquals(3, student.get(0).calculateGPASpecificSemester(student.get(0).getEnrolledCourses().get(1).getSemester()));
        assertEquals("Honours", student.get(0).getAcademicStanding());
        assertEquals("Semester{name='Spring', startDate=2024-02-03, endDate=2024-06-25, year=2024}: 3.0\n" + 
        "Semester{name='Fall', startDate=2023-09-01, endDate=2023-12-15, year=2023}: 3.5"
                , student.get(0).calculateSemesterGPAs());
        assertEquals("Semester{name='Spring', startDate=2024-02-03, endDate=2024-06-25, year=2024}: Honours\n" + 
        "Semester{name='Fall', startDate=2023-09-01, endDate=2023-12-15, year=2023}: Highest Honours", student.get(0).generateAcademicStandingReport());
        //Parallel 
        assertEquals(3.2857142857142856, student.get(0).calculateGPAs());
    }

    /*
     * Test - 14
     */
    Faculty assignedFaculty14;
    @Test
    void checkEnrollStudentWithoutConflictPre() throws FileNotFoundException{
        File file = new File("src/test/resources/Test14.txt");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String[] array = sc.nextLine().split(";");
            if(array[0].equals("Course")){
                String courseName = array[1];
                int credits = Integer.parseInt(array[2]);
                assignedFaculty14 = registrar.createFaculty(array[3], array[4]);
                faculties.add(assignedFaculty14);
                boolean isDigit = false;
                for(int i =0; i<array[3].length();i++){
                    if (Character.isDigit(array[3].charAt(i))) {
                    assignedFaculty14 = faculties.get(Integer.parseInt(array[3]));
                    isDigit = true;
                    }
                }
                List<Course.MeetingSession> meetingSessions = parseMeetingSessions(array[5]);
                Semester semester = registrar.createSemester(array[7], LocalDate.parse(array[8]), LocalDate.parse(array[9]));
                List<Course> pre = new ArrayList<>();
                if(!array[6].equals("null")){
                    pre.add(courses.get(Integer.parseInt(array[6])));
                }
                registrar.createCourse(courseName, credits, assignedFaculty14, meetingSessions, pre, semester);
            }
            else if(array[0].equals("Student")){
                registrar.createStudent(array[1], array[2]);
            }
        }
        sc.close();
        registrar.registerStudentForCourse(student.get(0), courses.get(0));
        registrar.enterGrade(student.get(0), courses.get(0), "B+");
        registrar.registerStudentForCourse(student.get(0), courses.get(1));
        assertEquals("[B+]", student.get(0).getCourseGrades().values().toString());
    }

    /*
     * Test - 15
     */
    Faculty assignedFaculty15;
    @Test
    void checkBrowseAvailableCoursesForSpecificSemester() throws FileNotFoundException{
        File file = new File("src/test/resources/Test15.txt");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String[] array = sc.nextLine().split(";");
            String courseName = array[0];
            int credits = Integer.parseInt(array[1]);
            assignedFaculty15 = registrar.createFaculty(array[2], array[3]);
            faculties.add(assignedFaculty15);
            boolean isDigit = false;
            for(int i =0; i<array[2].length();i++){
                if (Character.isDigit(array[2].charAt(i))) {
                assignedFaculty15 = faculties.get(Integer.parseInt(array[2]));
                isDigit = true;
                }
            }
            List<Course.MeetingSession> meetingSessions = parseMeetingSessions(array[4]);
            Semester semester = registrar.createSemester(array[6], LocalDate.parse(array[7]), LocalDate.parse(array[8]));
            List<Course> pre = new ArrayList<>();
            if(!array[5].equals("null")){
            pre.add(courses.get(Integer.parseInt(array[5])));
            }
            registrar.createCourse(courseName, credits, assignedFaculty15, meetingSessions, pre, semester);
        }
        sc.close();
        assertEquals("Course{name='Software Engineering', credits=4, assignedFaculty=UserProfile{name='Dr. Iman', contactDetails='iman@example.com', role='Faculty'}, meetingSessions=[Monday 09:00 - 11:00, Tuesday 11:00 - 13:00]}", registrar.browseAvailableCourses(semesters.get(0)));
    }

    /*
     * Test - 16
     */
    Faculty assignedFaculty16;
    @Test
    void enterGradeForCourcesWithConflict() throws FileNotFoundException{
        File file = new File("src/test/resources/Test16.txt");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String[] array = sc.nextLine().split(";");
            if(array[0].equals("Course")){
                String courseName = array[1];
                int credits = Integer.parseInt(array[2]);
                assignedFaculty16 = registrar.createFaculty(array[3], array[4]);
                faculties.add(assignedFaculty16);
                boolean isDigit = false;
                for(int i =0; i<array[3].length();i++){
                    if (Character.isDigit(array[3].charAt(i))) {
                    assignedFaculty16 = faculties.get(Integer.parseInt(array[3]));
                    isDigit = true;
                    }
                }
                List<Course.MeetingSession> meetingSessions = parseMeetingSessions(array[5]);
                Semester semester = registrar.createSemester(array[7], LocalDate.parse(array[8]), LocalDate.parse(array[9]));
                List<Course> pre = new ArrayList<>();
                if(!array[6].equals("null")){
                    pre.add(courses.get(Integer.parseInt(array[6])));
                }
                registrar.createCourse(courseName, credits, assignedFaculty16, meetingSessions, pre, semester);
            }
            else if(array[0].equals("Student")){
                registrar.createStudent(array[1], array[2]);
            }
        }
        sc.close();
        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> registrar.enterGrade(student.get(0), courses.get(1),"A"));
        assertTrue(thrown.getMessage().contains("Cannot add grade. Student not enrolled in the course."));
    }

    /*
     * Test - 17
     */
    Faculty assignedFaculty17;
    @Test
    void facultyandRegistrarenterGrade() throws FileNotFoundException, UnauthorizedFacultyException{
        File file = new File("src/test/resources/Test17.txt");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String[] array = sc.nextLine().split(";");
            if(array[0].equals("Course")){
                String courseName = array[1];
                int credits = Integer.parseInt(array[2]);
                assignedFaculty17 = registrar.createFaculty(array[3], array[4]);
                faculties.add(assignedFaculty17);
                boolean isDigit = false;
                for(int i =0; i<array[3].length();i++){
                    if (Character.isDigit(array[3].charAt(i))) {
                    assignedFaculty17 = faculties.get(Integer.parseInt(array[3]));
                    isDigit = true;
                    }
                }
                List<Course.MeetingSession> meetingSessions = parseMeetingSessions(array[5]);
                Semester semester = registrar.createSemester(array[7], LocalDate.parse(array[8]), LocalDate.parse(array[9]));
                List<Course> pre = new ArrayList<>();
                if(!array[6].equals("null")){
                    pre.add(courses.get(Integer.parseInt(array[6])));
                }
                registrar.createCourse(courseName, credits, assignedFaculty17, meetingSessions, pre, semester);
            }
            else if(array[0].equals("Student")){
                registrar.createStudent(array[1], array[2]);
            }
        }
        sc.close();
        registrar.registerStudentForCourse(student.get(0), courses.get(0));
        registrar.registerStudentForCourse(student.get(0), courses.get(1));
        registrar.enterGrade(student.get(0), courses.get(0), "B+");
        faculties.get(0).enterGrade(student.get(0), courses.get(1), "B");
        assertEquals(3.2857142857142856, student.get(0).calculateCumEarnedGPA());
        assertEquals(3.5, student.get(0).calculateGPASpecificSemester(student.get(0).getEnrolledCourses().get(0).getSemester()));
        assertEquals(3.0, student.get(0).calculateGPASpecificSemester(student.get(0).getEnrolledCourses().get(1).getSemester()));
        assertEquals("Honours", student.get(0).getAcademicStanding());
        assertEquals( "Semester{name='Spring', startDate=2024-02-03, endDate=2024-06-25, year=2024}: 3.0\n"
        + "Semester{name='Fall', startDate=2023-09-01, endDate=2023-12-15, year=2023}: 3.5"
                , student.get(0).calculateSemesterGPAs());
        assertEquals("Semester{name='Spring', startDate=2024-02-03, endDate=2024-06-25, year=2024}: Honours\n" +
         "Semester{name='Fall', startDate=2023-09-01, endDate=2023-12-15, year=2023}: Highest Honours" , student.get(0).generateAcademicStandingReport());
        //Parallel 
        assertEquals(3.2857142857142856, student.get(0).calculateGPAs());
    }

    /*
     * Test - 18
     */
    Faculty assignedFaculty2;
    @Test
    void testCreateCourseFromRegitrarWithConflictTotalTime() throws FileNotFoundException {
        File file = new File("src/test/resources/Test18.txt");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String[] array = sc.nextLine().split(";");
            String courseName = array[0];
            int credits = Integer.parseInt(array[1]);
            assignedFaculty2 = registrar.createFaculty(array[2], array[3]);
            boolean isDigit = false;
            for(int i =0; i<array[2].length();i++){
                if (Character.isDigit(array[2].charAt(i))) {
                assignedFaculty2 = faculties.get(Integer.parseInt(array[2]));
                isDigit = true;
                }
            }
            List<Course.MeetingSession> meetingSessions = parseMeetingSessions(array[4]);
            Semester semester = registrar.createSemester(array[6], LocalDate.parse(array[7]), LocalDate.parse(array[8]));
            List<Course> pre = new ArrayList<>();
            if(!array[5].equals("null")){
            pre.add(courses.get(Integer.parseInt(array[5])));
            }
            IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> registrar.createCourse(courseName, credits, assignedFaculty2, meetingSessions, pre, semester));
            assertTrue(thrown.getMessage().contains("Total hours of meeting sessions (2) do not match the course credits (3)"));
        }
        sc.close();
    }
    /*
     * Test - 19
     */
    Faculty assignedFaculty19;
    @Test
    void testCreateCourseFromRegitrarWithConflictalreadyEnroll() throws FileNotFoundException {
        File file = new File("src/test/resources/Test19.txt");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String[] array = sc.nextLine().split(";");
            if(array[0].equals("Course")){
                String courseName = array[1];
                int credits = Integer.parseInt(array[2]);
                assignedFaculty19 = registrar.createFaculty(array[3], array[4]);
                faculties.add(assignedFaculty19);
                boolean isDigit = false;
                for(int i =0; i<array[3].length();i++){
                    if (Character.isDigit(array[3].charAt(i))) {
                    assignedFaculty19 = faculties.get(Integer.parseInt(array[3]));
                    isDigit = true;
                    }
                }
                List<Course.MeetingSession> meetingSessions = parseMeetingSessions(array[5]);
                Semester semester = registrar.createSemester(array[7], LocalDate.parse(array[8]), LocalDate.parse(array[9]));
                List<Course> pre = new ArrayList<>();
                if(!array[6].equals("null")){
                    pre.add(courses.get(Integer.parseInt(array[6])));
                }
                registrar.createCourse(courseName, credits, assignedFaculty19, meetingSessions, pre, semester);
            }
            else if(array[0].equals("Student")){
                registrar.createStudent(array[1], array[2]);
            }
        }
        sc.close();
        registrar.registerStudentForCourse(student.get(0), courses.get(0));
        registrar.registerStudentForCourse(student.get(0), courses.get(1));
        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> registrar.registerStudentForCourse(student.get(0), courses.get(0)));
            assertTrue(thrown.getMessage().contains("Already enrolled in the course."));
    }
}