package com.sms.report;

import com.sms.model.Student;

import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * HtmlReportGenerator
 * Generates a beautiful HTML + CSS dashboard from student data.
 * Opens automatically in the default browser.
 */
public class HtmlReportGenerator {

    private static final String REPORT_FILE = "data/report.html";

    public static void generate(List<Student> students) {
        ensureDataDir();

        double avgGpa   = students.stream().mapToDouble(Student::getGpa).average().orElse(0);
        double topGpa   = students.stream().mapToDouble(Student::getGpa).max().orElse(0);
        String topName  = students.stream()
                            .filter(s -> s.getGpa() == topGpa)
                            .map(Student::getName)
                            .findFirst().orElse("N/A");
        String now      = LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));

        StringBuilder html = new StringBuilder();
        html.append(getHeader(students.size(), avgGpa, topGpa, topName, now));
        html.append(getTableRows(students));
        html.append(getFooter());

        try (PrintWriter pw = new PrintWriter(new FileWriter(REPORT_FILE))) {
            pw.print(html);
            System.out.println("\n  ✅  HTML report generated: " + REPORT_FILE);
            openInBrowser();
        } catch (IOException e) {
            System.err.println("  ❌  Could not create report: " + e.getMessage());
        }
    }

    // ─── Open browser ────────────────────────────────────────────────────────

    private static void openInBrowser() {
        try {
            File file = new File(REPORT_FILE);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(file.toURI());
                System.out.println("  🌐  Opened in your browser!");
            } else {
                System.out.println("  📄  Open this file manually: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            System.out.println("  📄  Report saved at: " + new File(REPORT_FILE).getAbsolutePath());
        }
    }

    private static void ensureDataDir() {
        new File("data").mkdirs();
    }

    // ─── HTML Header + CSS ───────────────────────────────────────────────────

    private static String getHeader(int total, double avgGpa, double topGpa,
                                    String topName, String now) {
        return """
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Student Management System — Dashboard</title>
  <link href="https://fonts.googleapis.com/css2?family=Syne:wght@400;600;700;800&family=DM+Sans:wght@300;400;500&display=swap" rel="stylesheet"/>
  <style>
    *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

    :root {
      --bg:        #0b0f1a;
      --surface:   #111827;
      --card:      #1a2236;
      --border:    #1e2d45;
      --accent:    #3b82f6;
      --accent2:   #8b5cf6;
      --accent3:   #10b981;
      --accent4:   #f59e0b;
      --text:      #e2e8f0;
      --muted:     #64748b;
      --danger:    #ef4444;
    }

    body {
      font-family: 'DM Sans', sans-serif;
      background: var(--bg);
      color: var(--text);
      min-height: 100vh;
      padding: 2rem;
    }

    /* ── Animated gradient background ── */
    body::before {
      content: '';
      position: fixed;
      inset: 0;
      background:
        radial-gradient(ellipse 80% 60% at 20% 10%, rgba(59,130,246,.12) 0%, transparent 60%),
        radial-gradient(ellipse 60% 50% at 80% 90%, rgba(139,92,246,.10) 0%, transparent 60%);
      pointer-events: none;
      z-index: 0;
    }

    .wrapper { position: relative; z-index: 1; max-width: 1200px; margin: 0 auto; }

    /* ── Header ── */
    header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 2.5rem;
      flex-wrap: wrap;
      gap: 1rem;
    }
    .logo-area h1 {
      font-family: 'Syne', sans-serif;
      font-size: 2rem;
      font-weight: 800;
      background: linear-gradient(135deg, #3b82f6, #8b5cf6);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      line-height: 1;
    }
    .logo-area p { color: var(--muted); font-size: .85rem; margin-top: .4rem; }

    .badge {
      background: var(--card);
      border: 1px solid var(--border);
      border-radius: 99px;
      padding: .4rem 1rem;
      font-size: .78rem;
      color: var(--muted);
    }
    .badge span { color: var(--accent); font-weight: 600; }

    /* ── Stat Cards ── */
    .stats {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));
      gap: 1.2rem;
      margin-bottom: 2.5rem;
    }
    .stat-card {
      background: var(--card);
      border: 1px solid var(--border);
      border-radius: 16px;
      padding: 1.5rem;
      position: relative;
      overflow: hidden;
      transition: transform .2s, border-color .2s;
    }
    .stat-card:hover { transform: translateY(-3px); border-color: var(--accent); }
    .stat-card::before {
      content: '';
      position: absolute;
      top: 0; left: 0; right: 0;
      height: 3px;
      border-radius: 16px 16px 0 0;
    }
    .stat-card.blue::before  { background: linear-gradient(90deg,#3b82f6,#60a5fa); }
    .stat-card.purple::before{ background: linear-gradient(90deg,#8b5cf6,#a78bfa); }
    .stat-card.green::before { background: linear-gradient(90deg,#10b981,#34d399); }
    .stat-card.amber::before { background: linear-gradient(90deg,#f59e0b,#fbbf24); }

    .stat-icon {
      width: 42px; height: 42px;
      border-radius: 10px;
      display: flex; align-items: center; justify-content: center;
      font-size: 1.3rem;
      margin-bottom: 1rem;
    }
    .blue .stat-icon   { background: rgba(59,130,246,.15); }
    .purple .stat-icon { background: rgba(139,92,246,.15); }
    .green .stat-icon  { background: rgba(16,185,129,.15); }
    .amber .stat-icon  { background: rgba(245,158,11,.15); }

    .stat-value {
      font-family: 'Syne', sans-serif;
      font-size: 2rem;
      font-weight: 800;
      line-height: 1;
      margin-bottom: .3rem;
    }
    .blue .stat-value   { color: #60a5fa; }
    .purple .stat-value { color: #a78bfa; }
    .green .stat-value  { color: #34d399; }
    .amber .stat-value  { color: #fbbf24; }

    .stat-label { color: var(--muted); font-size: .83rem; }

    /* ── Table Card ── */
    .table-card {
      background: var(--card);
      border: 1px solid var(--border);
      border-radius: 20px;
      overflow: hidden;
    }
    .table-card-header {
      padding: 1.4rem 1.8rem;
      display: flex;
      align-items: center;
      justify-content: space-between;
      border-bottom: 1px solid var(--border);
      flex-wrap: wrap;
      gap: .8rem;
    }
    .table-card-header h2 {
      font-family: 'Syne', sans-serif;
      font-size: 1.1rem;
      font-weight: 700;
    }

    /* Search */
    .search-box {
      display: flex;
      align-items: center;
      gap: .5rem;
      background: var(--surface);
      border: 1px solid var(--border);
      border-radius: 10px;
      padding: .45rem .9rem;
    }
    .search-box input {
      background: none;
      border: none;
      outline: none;
      color: var(--text);
      font-family: 'DM Sans', sans-serif;
      font-size: .88rem;
      width: 180px;
    }
    .search-box input::placeholder { color: var(--muted); }
    .search-icon { color: var(--muted); font-size: .9rem; }

    /* Table */
    .table-wrap { overflow-x: auto; }
    table { width: 100%; border-collapse: collapse; }
    thead th {
      background: var(--surface);
      padding: .9rem 1.4rem;
      text-align: left;
      font-size: .75rem;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: .08em;
      color: var(--muted);
      white-space: nowrap;
    }
    tbody tr {
      border-top: 1px solid var(--border);
      transition: background .15s;
    }
    tbody tr:hover { background: rgba(59,130,246,.05); }
    tbody td {
      padding: 1rem 1.4rem;
      font-size: .88rem;
      white-space: nowrap;
    }

    /* Avatar */
    .avatar {
      width: 34px; height: 34px;
      border-radius: 50%;
      display: flex; align-items: center; justify-content: center;
      font-family: 'Syne', sans-serif;
      font-weight: 700;
      font-size: .85rem;
      color: #fff;
      flex-shrink: 0;
    }
    .name-cell { display: flex; align-items: center; gap: .75rem; }
    .name-text  { font-weight: 500; }
    .email-text { color: var(--muted); font-size: .8rem; }

    /* GPA badge */
    .gpa-badge {
      display: inline-block;
      padding: .25rem .75rem;
      border-radius: 99px;
      font-size: .8rem;
      font-weight: 600;
    }
    .gpa-high   { background: rgba(16,185,129,.15); color: #34d399; }
    .gpa-mid    { background: rgba(59,130,246,.15);  color: #60a5fa; }
    .gpa-low    { background: rgba(245,158,11,.15);  color: #fbbf24; }

    /* Course chip */
    .course-chip {
      background: rgba(139,92,246,.12);
      color: #a78bfa;
      border-radius: 8px;
      padding: .2rem .7rem;
      font-size: .8rem;
    }

    /* ID */
    .id-num {
      color: var(--muted);
      font-family: 'Syne', sans-serif;
      font-size: .82rem;
    }

    /* Empty state */
    .empty {
      text-align: center;
      padding: 4rem 2rem;
      color: var(--muted);
    }
    .empty-icon { font-size: 3rem; margin-bottom: 1rem; }

    /* Footer */
    footer {
      text-align: center;
      margin-top: 2.5rem;
      color: var(--muted);
      font-size: .8rem;
    }
    footer span { color: var(--accent); }
  </style>
</head>
<body>
<div class="wrapper">

  <header>
    <div class="logo-area">
      <h1>📚 Student Dashboard</h1>
      <p>Student Management System &nbsp;·&nbsp; Generated on """ + now + """
</p>
    </div>
    <div class="badge">Java App &nbsp;·&nbsp; <span>v1.0</span></div>
  </header>

  <!-- Stats -->
  <div class="stats">
    <div class="stat-card blue">
      <div class="stat-icon">👥</div>
      <div class="stat-value">""" + total + """
</div>
      <div class="stat-label">Total Students</div>
    </div>
    <div class="stat-card purple">
      <div class="stat-icon">📊</div>
      <div class="stat-value">""" + String.format("%.2f", avgGpa) + """
</div>
      <div class="stat-label">Average GPA</div>
    </div>
    <div class="stat-card green">
      <div class="stat-icon">🏆</div>
      <div class="stat-value">""" + String.format("%.2f", topGpa) + """
</div>
      <div class="stat-label">Top GPA</div>
    </div>
    <div class="stat-card amber">
      <div class="stat-icon">⭐</div>
      <div class="stat-value" style="font-size:1.1rem;padding-top:.45rem">""" + topName + """
</div>
      <div class="stat-label">Top Student</div>
    </div>
  </div>

  <!-- Table -->
  <div class="table-card">
    <div class="table-card-header">
      <h2>All Students</h2>
      <div class="search-box">
        <span class="search-icon">🔍</span>
        <input type="text" id="searchInput" placeholder="Search students..." onkeyup="filterTable()"/>
      </div>
    </div>
    <div class="table-wrap">
      <table id="studentTable">
        <thead>
          <tr>
            <th>ID</th>
            <th>Student</th>
            <th>Age</th>
            <th>Phone</th>
            <th>Course</th>
            <th>GPA</th>
          </tr>
        </thead>
        <tbody>
""";
    }

    // ─── Table Rows ──────────────────────────────────────────────────────────

    private static String getTableRows(List<Student> students) {
        if (students.isEmpty()) {
            return """
              <tr><td colspan="6">
                <div class="empty">
                  <div class="empty-icon">📭</div>
                  <p>No students found. Add students from the Java app.</p>
                </div>
              </td></tr>
            """;
        }

        String[] colors = {"#3b82f6","#8b5cf6","#10b981","#f59e0b","#ef4444",
                           "#06b6d4","#ec4899","#84cc16","#f97316","#6366f1"};

        StringBuilder rows = new StringBuilder();
        for (Student s : students) {
            String initials = getInitials(s.getName());
            String color    = colors[Math.abs(s.getName().hashCode()) % colors.length];
            String gpaClass = s.getGpa() >= 8.5 ? "gpa-high" : s.getGpa() >= 7.0 ? "gpa-mid" : "gpa-low";

            rows.append(String.format("""
              <tr>
                <td><span class="id-num">#%03d</span></td>
                <td>
                  <div class="name-cell">
                    <div class="avatar" style="background:%s">%s</div>
                    <div>
                      <div class="name-text">%s</div>
                      <div class="email-text">%s</div>
                    </div>
                  </div>
                </td>
                <td>%d yrs</td>
                <td>%s</td>
                <td><span class="course-chip">%s</span></td>
                <td><span class="gpa-badge %s">%.2f</span></td>
              </tr>
            """, s.getId(), color, initials, s.getName(), s.getEmail(),
                 s.getAge(), s.getPhone(), s.getCourse(), gpaClass, s.getGpa()));
        }
        return rows.toString();
    }

    private static String getInitials(String name) {
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2) return ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }

    // ─── Footer + JS ─────────────────────────────────────────────────────────

    private static String getFooter() {
        return """
        </tbody>
      </table>
    </div>
  </div>

  <footer>
    Generated by <span>Student Management System</span> &nbsp;·&nbsp; Built with Java ☕
  </footer>

</div>

<script>
  function filterTable() {
    const input  = document.getElementById('searchInput').value.toLowerCase();
    const rows   = document.querySelectorAll('#studentTable tbody tr');
    rows.forEach(row => {
      const text = row.textContent.toLowerCase();
      row.style.display = text.includes(input) ? '' : 'none';
    });
  }
</script>
</body>
</html>
""";
    }
}
