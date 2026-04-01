package com.sms.model;

import java.io.Serializable;

/**
 * Student Model — implements Serializable so data saves to file automatically.
 * No database needed!
 */
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    private int    id;
    private String name;
    private int    age;
    private String email;
    private String phone;
    private String course;
    private double gpa;

    public Student() {}

    public Student(int id, String name, int age, String email, String phone, String course, double gpa) {
        this.id     = id;
        this.name   = name;
        this.age    = age;
        this.email  = email;
        this.phone  = phone;
        this.course = course;
        this.gpa    = gpa;
    }

    public int    getId()     { return id; }
    public String getName()   { return name; }
    public int    getAge()    { return age; }
    public String getEmail()  { return email; }
    public String getPhone()  { return phone; }
    public String getCourse() { return course; }
    public double getGpa()    { return gpa; }

    public void setId(int id)           { this.id = id; }
    public void setName(String name)    { this.name = name; }
    public void setAge(int age)         { this.age = age; }
    public void setEmail(String email)  { this.email = email; }
    public void setPhone(String phone)  { this.phone = phone; }
    public void setCourse(String c)     { this.course = c; }
    public void setGpa(double gpa)      { this.gpa = gpa; }

    @Override
    public String toString() {
        return String.format(
            "\n  ┌────────────────────────────────────────┐\n" +
            "  │  ID     : %-30d│\n" +
            "  │  Name   : %-30s│\n" +
            "  │  Age    : %-30d│\n" +
            "  │  Email  : %-30s│\n" +
            "  │  Phone  : %-30s│\n" +
            "  │  Course : %-30s│\n" +
            "  │  GPA    : %-30.2f│\n" +
            "  └────────────────────────────────────────┘",
            id, name, age, email, phone, course, gpa
        );
    }
}
