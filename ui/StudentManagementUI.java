package com.sms.ui;

import com.sms.dao.StudentDAO;
import com.sms.model.Student;
import com.sms.report.HtmlReportGenerator;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class StudentManagementUI {

    private final StudentDAO dao = new StudentDAO();
    private final Scanner    sc  = new Scanner(System.in);

    public void run() {
        printBanner();
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("  Enter choice (1-8): ");
            System.out.println();
            switch (choice) {
                case 1  -> addStudent();
                case 2  -> viewAll();
                case 3  -> searchById();
                case 4  -> searchByName();
                case 5  -> updateStudent();
                case 6  -> deleteStudent();
                case 7  -> openHtmlDashboard();
                case 8  -> { running = false; exit(); }
                default -> System.out.println("  ⚠️  Invalid choice! Enter 1 to 8.\n");
            }
        }
    }

    private void printBanner() {
        System.out.println();
        System.out.println("  ╔════════════════════════════════════════════╗");
        System.out.println("  ║     STUDENT MANAGEMENT SYSTEM  v1.0       ║");
        System.out.println("  ║       Java | File Storage | CRUD          ║");
        System.out.println("  ║    Data saves automatically — No SQL!     ║");
        System.out.println("  ╚════════════════════════════════════════════╝");
        System.out.println();
    }

    private void printMenu() {
        System.out.println("  ┌────────────────────────────────────────┐");
        System.out.println("  │              MAIN  MENU                │");
        System.out.println("  ├────────────────────────────────────────┤");
        System.out.println("  │  1.  ➕  Add New Student               │");
        System.out.println("  │  2.  📋  View All Students             │");
        System.out.println("  │  3.  🔍  Search by ID                  │");
        System.out.println("  │  4.  🔎  Search by Name                │");
        System.out.println("  │  5.  ✏️   Update Student                │");
        System.out.println("  │  6.  🗑️   Delete Student                │");
        System.out.println("  │  7.  🌐  View HTML Dashboard           │");
        System.out.println("  │  8.  🚪  Exit                          │");
        System.out.println("  └────────────────────────────────────────┘");
    }

    private void openHtmlDashboard() {
        System.out.println("  ╔══ HTML DASHBOARD ═══════════════════════╗\n");
        List<Student> students = dao.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("  ℹ️  No students to display. Add students first!\n");
            return;
        }
        HtmlReportGenerator.generate(students);
        System.out.println();
    }

    private void addStudent() {
        System.out.println("  ╔══ ADD NEW STUDENT ══════════════════════╗\n");
        String name   = readString("  Name   : ");
        int    age    = readInt   ("  Age    : ");
        String email  = readString("  Email  : ");
        String phone  = readString("  Phone  : ");
        String course = readString("  Course : ");
        double gpa    = readDouble("  GPA    : ");
        dao.addStudent(new Student(0, name, age, email, phone, course, gpa));
        System.out.println();
    }

    private void viewAll() {
        List<Student> list = dao.getAllStudents();
        System.out.println("  ╔══ ALL STUDENTS (" + list.size() + " records) ═══════════╗\n");
        if (list.isEmpty()) { System.out.println("  ℹ️  No students found.\n"); return; }
        printHeader(); list.forEach(this::printRow); printFooter();
    }

    private void searchById() {
        System.out.println("  ╔══ SEARCH BY ID ═════════════════════════╗");
        int id = readInt("  Enter Student ID: ");
        Student s = dao.getStudentById(id);
        System.out.println(s != null ? s : "  ⚠️  No student found with ID: " + id);
        System.out.println();
    }

    private void searchByName() {
        System.out.println("  ╔══ SEARCH BY NAME ═══════════════════════╗");
        String kw = readString("  Enter name to search: ");
        List<Student> results = dao.searchByName(kw);
        if (results.isEmpty()) { System.out.println("  ℹ️  No students found matching: " + kw + "\n"); return; }
        System.out.println("  Found " + results.size() + " result(s):\n");
        printHeader(); results.forEach(this::printRow); printFooter();
    }

    private void updateStudent() {
        System.out.println("  ╔══ UPDATE STUDENT ═══════════════════════╗");
        int id = readInt("  Enter Student ID to update: ");
        Student s = dao.getStudentById(id);
        if (s == null) { System.out.println("  ⚠️  Student not found!\n"); return; }
        System.out.println("\n  Current details:"); System.out.println(s);
        System.out.println("\n  Enter new values (press Enter to keep existing):\n");
        String name   = opt("  Name   [" + s.getName()   + "]: ", s.getName());
        int    age    = optInt("  Age  [" + s.getAge()    + "]: ", s.getAge());
        String email  = opt("  Email  [" + s.getEmail()  + "]: ", s.getEmail());
        String phone  = opt("  Phone  [" + s.getPhone()  + "]: ", s.getPhone());
        String course = opt("  Course [" + s.getCourse() + "]: ", s.getCourse());
        double gpa    = optDbl("  GPA   [" + s.getGpa()   + "]: ", s.getGpa());
        dao.updateStudent(new Student(id, name, age, email, phone, course, gpa));
        System.out.println();
    }

    private void deleteStudent() {
        System.out.println("  ╔══ DELETE STUDENT ═══════════════════════╗");
        int id = readInt("  Enter Student ID to delete: ");
        Student s = dao.getStudentById(id);
        if (s == null) { System.out.println("  ⚠️  Student not found!\n"); return; }
        System.out.println(s);
        String confirm = readString("\n  Confirm delete? (yes / no): ");
        if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y"))
            dao.deleteStudent(id);
        else System.out.println("  ❌  Deletion cancelled.");
        System.out.println();
    }

    private void exit() {
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║   Thank you! Data saved. Goodbye 👋     ║");
        System.out.println("  ╚══════════════════════════════════════════╝\n");
        sc.close();
    }

    private void printHeader() {
        System.out.println("  " + "─".repeat(86));
        System.out.printf("  │ %-4s │ %-18s │ %-4s │ %-28s │ %-15s │ %-5s │%n",
            "ID","Name","Age","Email","Course","GPA");
        System.out.println("  " + "─".repeat(86));
    }
    private void printRow(Student s) {
        System.out.printf("  │ %-4d │ %-18s │ %-4d │ %-28s │ %-15s │ %-5.2f │%n",
            s.getId(),s.getName(),s.getAge(),s.getEmail(),s.getCourse(),s.getGpa());
    }
    private void printFooter() { System.out.println("  " + "─".repeat(86) + "\n"); }

    private String readString(String p) { System.out.print(p); return sc.nextLine().trim(); }
    private int readInt(String p) {
        while(true){System.out.print(p);try{int v=sc.nextInt();sc.nextLine();return v;}
        catch(InputMismatchException e){sc.nextLine();System.out.println("  ⚠️  Enter a valid number.");}}
    }
    private double readDouble(String p) {
        while(true){System.out.print(p);try{double v=sc.nextDouble();sc.nextLine();return v;}
        catch(InputMismatchException e){sc.nextLine();System.out.println("  ⚠️  Enter a valid decimal.");}}
    }
    private String opt(String p,String d){System.out.print(p);String v=sc.nextLine().trim();return v.isEmpty()?d:v;}
    private int optInt(String p,int d){System.out.print(p);String v=sc.nextLine().trim();try{return v.isEmpty()?d:Integer.parseInt(v);}catch(Exception e){return d;}}
    private double optDbl(String p,double d){System.out.print(p);String v=sc.nextLine().trim();try{return v.isEmpty()?d:Double.parseDouble(v);}catch(Exception e){return d;}}
}
