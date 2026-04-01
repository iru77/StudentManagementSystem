package com.sms.dao;

import com.sms.db.FileDatabase;
import com.sms.model.Student;

import java.util.List;
import java.util.stream.Collectors;

/**
 * StudentDAO — Data Access Object
 * All CRUD operations — reads/writes to local file (no MySQL needed).
 */
public class StudentDAO {

    // ──────────────────────────── CREATE ─────────────────────────────────────
    public boolean addStudent(Student student) {
        List<Student> all = FileDatabase.loadAll();

        // Check duplicate email
        boolean emailExists = all.stream()
            .anyMatch(s -> s.getEmail().equalsIgnoreCase(student.getEmail()));
        if (emailExists) {
            System.out.println("  ⚠️  Email already exists: " + student.getEmail());
            return false;
        }

        student.setId(FileDatabase.nextId());
        all.add(student);

        if (FileDatabase.saveAll(all)) {
            System.out.println("  ✅  Student added! Assigned ID: " + student.getId());
            return true;
        }
        return false;
    }

    // ──────────────────────────── READ ALL ───────────────────────────────────
    public List<Student> getAllStudents() {
        return FileDatabase.loadAll();
    }

    // ──────────────────────────── READ BY ID ─────────────────────────────────
    public Student getStudentById(int id) {
        return FileDatabase.loadAll().stream()
            .filter(s -> s.getId() == id)
            .findFirst()
            .orElse(null);
    }

    // ──────────────────────────── SEARCH BY NAME ─────────────────────────────
    public List<Student> searchByName(String keyword) {
        String kw = keyword.toLowerCase();
        return FileDatabase.loadAll().stream()
            .filter(s -> s.getName().toLowerCase().contains(kw))
            .collect(Collectors.toList());
    }

    // ──────────────────────────── UPDATE ─────────────────────────────────────
    public boolean updateStudent(Student updated) {
        List<Student> all = FileDatabase.loadAll();
        boolean found = false;

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == updated.getId()) {
                all.set(i, updated);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("  ⚠️  No student found with ID: " + updated.getId());
            return false;
        }

        if (FileDatabase.saveAll(all)) {
            System.out.println("  ✅  Student ID " + updated.getId() + " updated successfully.");
            return true;
        }
        return false;
    }

    // ──────────────────────────── DELETE ─────────────────────────────────────
    public boolean deleteStudent(int id) {
        List<Student> all = FileDatabase.loadAll();
        boolean removed = all.removeIf(s -> s.getId() == id);

        if (!removed) {
            System.out.println("  ⚠️  No student found with ID: " + id);
            return false;
        }

        if (FileDatabase.saveAll(all)) {
            System.out.println("  ✅  Student ID " + id + " deleted successfully.");
            return true;
        }
        return false;
    }
}
