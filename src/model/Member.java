package model;

/**
 * Model class representing a library Member (card holder).
 */
public class Member {

    private int     memberId;
    private String  fullName;
    private String  email;
    private String  phone;
    private String  address;
    private String  joinedDate;
    private boolean isActive;

    // ── Constructors ──────────────────────────────────────────

    public Member() {}

    public Member(int memberId, String fullName, String email,
                  String phone, String address, String joinedDate, boolean isActive) {
        this.memberId   = memberId;
        this.fullName   = fullName;
        this.email      = email;
        this.phone      = phone;
        this.address    = address;
        this.joinedDate = joinedDate;
        this.isActive   = isActive;
    }

    // Constructor without ID (for INSERT)
    public Member(String fullName, String email, String phone, String address) {
        this.fullName = fullName;
        this.email    = email;
        this.phone    = phone;
        this.address  = address;
        this.isActive = true;
    }

    // ── Getters & Setters ─────────────────────────────────────

    public int     getMemberId()   { return memberId; }
    public void    setMemberId(int memberId) { this.memberId = memberId; }

    public String  getFullName()   { return fullName; }
    public void    setFullName(String fullName) { this.fullName = fullName; }

    public String  getEmail()      { return email; }
    public void    setEmail(String email) { this.email = email; }

    public String  getPhone()      { return phone; }
    public void    setPhone(String phone) { this.phone = phone; }

    public String  getAddress()    { return address; }
    public void    setAddress(String address) { this.address = address; }

    public String  getJoinedDate() { return joinedDate; }
    public void    setJoinedDate(String joinedDate) { this.joinedDate = joinedDate; }

    public boolean isActive()      { return isActive; }
    public void    setActive(boolean active) { isActive = active; }

    // ── toString ──────────────────────────────────────────────

    @Override
    public String toString() {
        return "Member{" +
               "memberId=" + memberId +
               ", fullName='" + fullName + '\'' +
               ", email='" + email + '\'' +
               ", phone='" + phone + '\'' +
               ", isActive=" + isActive +
               '}';
    }
}
