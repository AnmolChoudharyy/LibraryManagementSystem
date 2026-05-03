package model;

/**
 * Model class representing a Staff member (librarian or admin).
 * role: "admin" | "librarian"
 */
public class Staff {

    private int    staffId;
    private String fullName;
    private String username;
    private String password;
    private String role;
    private String email;
    private String phone;
    private String createdAt;

    // ── Constructors ──────────────────────────────────────────

    public Staff() {}

    public Staff(int staffId, String fullName, String username,
                 String password, String role, String email,
                 String phone, String createdAt) {
        this.staffId   = staffId;
        this.fullName  = fullName;
        this.username  = username;
        this.password  = password;
        this.role      = role;
        this.email     = email;
        this.phone     = phone;
        this.createdAt = createdAt;
    }

    // Constructor for new staff (no ID)
    public Staff(String fullName, String username, String password,
                 String role, String email, String phone) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.role     = role;
        this.email    = email;
        this.phone    = phone;
    }

    // ── Getters & Setters ─────────────────────────────────────

    public int    getStaffId()   { return staffId; }
    public void   setStaffId(int staffId) { this.staffId = staffId; }

    public String getFullName()  { return fullName; }
    public void   setFullName(String fullName) { this.fullName = fullName; }

    public String getUsername()  { return username; }
    public void   setUsername(String username) { this.username = username; }

    public String getPassword()  { return password; }
    public void   setPassword(String password) { this.password = password; }

    public String getRole()      { return role; }
    public void   setRole(String role) { this.role = role; }

    public String getEmail()     { return email; }
    public void   setEmail(String email) { this.email = email; }

    public String getPhone()     { return phone; }
    public void   setPhone(String phone) { this.phone = phone; }

    public String getCreatedAt() { return createdAt; }
    public void   setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    // ── Helpers ───────────────────────────────────────────────

    public boolean isAdmin() { return "admin".equalsIgnoreCase(role); }

    // ── toString ──────────────────────────────────────────────

    @Override
    public String toString() {
        return "Staff{" +
               "staffId=" + staffId +
               ", fullName='" + fullName + '\'' +
               ", username='" + username + '\'' +
               ", role='" + role + '\'' +
               '}';
    }
}
