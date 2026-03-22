package helper;

public class UserInfo {

    private int    userId;
    private String username;
    private String email;
    private String role;

    public UserInfo() {}

    public UserInfo(int userId, String username, String email, String role) {
        this.userId   = userId;
        this.username = username;
        this.email    = email;
        this.role     = role;
    }

    public int    getUserId()  { return userId; }
    public String getUsername(){ return username; }
    public String getEmail()   { return email; }
    public String getRole()    { return role; }

    public void setUserId(int userId)        { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email)       { this.email = email; }
    public void setRole(String role)         { this.role = role; }
}
