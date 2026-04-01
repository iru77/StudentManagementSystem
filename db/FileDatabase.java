package com.sms.db;

import com.sms.model.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileDatabase — replaces MySQL entirely.
 *
 * ✅ No MySQL needed
 * ✅ No JAR download needed
 * ✅ No configuration needed
 *
 * Data is saved in:  data/students.dat  (auto-created on first run)
 * ID counter saved in: data/counter.dat (auto-created on first run)
 */
public class FileDatabase {

    private static final String DATA_DIR      = "data";
    private static final String STUDENTS_FILE = DATA_DIR + File.separator + "students.dat";
    private static final String COUNTER_FILE  = DATA_DIR + File.separator + "counter.dat";

    // ──────────── Load all students from file ────────────────────────────────
    @SuppressWarnings("unchecked")
    public static List<Student> loadAll() {
        File file = new File(STUDENTS_FILE);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Student>) ois.readObject();
        } catch (Exception e) {
            System.err.println("⚠️  Could not load data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ──────────── Save all students to file ──────────────────────────────────
    public static boolean saveAll(List<Student> students) {
        ensureDataDir();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STUDENTS_FILE))) {
            oos.writeObject(students);
            return true;
        } catch (IOException e) {
            System.err.println("❌  Could not save data: " + e.getMessage());
            return false;
        }
    }

    // ──────────── Auto-increment ID ──────────────────────────────────────────
    public static int nextId() {
        ensureDataDir();
        int current = 1;
        File f = new File(COUNTER_FILE);

        if (f.exists()) {
            try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
                current = dis.readInt();
            } catch (IOException ignored) {}
        }

        int next = current + 1;
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(f))) {
            dos.writeInt(next);
        } catch (IOException e) {
            System.err.println("⚠️  Counter error: " + e.getMessage());
        }
        return current;
    }

    // ──────────── Ensure data/ folder exists ─────────────────────────────────
    private static void ensureDataDir() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();
    }
}
