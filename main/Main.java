package com.sms.main;

import com.sms.ui.StudentManagementUI;

/**
 * ╔════════════════════════════════════════════╗
 * ║     STUDENT MANAGEMENT SYSTEM  v1.0       ║
 * ║       Java | File Storage | CRUD          ║
 * ╚════════════════════════════════════════════╝
 *
 *  HOW TO RUN (No MySQL needed!):
 *  ─────────────────────────────
 *  Option A — IntelliJ / Eclipse:
 *    Just open project and run this Main.java
 *
 *  Option B — Command Line:
 *    1. Compile:
 *       javac -d out src/com/sms/model/Student.java
 *                    src/com/sms/db/FileDatabase.java
 *                    src/com/sms/dao/StudentDAO.java
 *                    src/com/sms/ui/StudentManagementUI.java
 *                    src/com/sms/main/Main.java
 *
 *    2. Run:
 *       java -cp out com.sms.main.Main
 *
 *  Data is stored in: data/students.dat (auto-created)
 */
public class Main {
    public static void main(String[] args) {
        new StudentManagementUI().run();
    }
}
