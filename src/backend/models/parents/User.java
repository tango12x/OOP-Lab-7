package backend.models.parents;

public abstract class User {
    private String userId;
    private String role;
    private String username;
    private String email;
    private String passwordHash;

    //CLASS CONSTRUCTOR IN CASE THE USERID IS GIVEN
    public User(String userId, String role, String username, String email, String passwordHash) {
        this.userId = userId;
        this.role = role;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }
    //OVERLOADING CLASS CONSTRUCTOR IN CASE OF ID IS NOT GIVEN
    public User( String role, String username, String email, String passwordHash) {
        this.role = role;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    //standard getters
    public String getUserId() { return userId; }
    public String getRole() { return role; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public void setUserId(String userId){this.userId = userId ; }
    public void setRole (String role){this.role = role;}
    public void setUsername(String username){this.username = username;}
    public void setEmail(String email){this.email = email ; }
    public void setPasswordHash(String passwordHash){this.passwordHash = passwordHash;}

}
