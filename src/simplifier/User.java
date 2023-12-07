package simplifier;

/**
 *
 * @author Tiago
 */
// Abstract class for user
class User {
    private String fullName;
    private String email;
    private String password;
    private int userId;

    public User(String username, String fullName, String email, String password, int userId) {
        
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.userId = userId;
    }

    // Getter and setter methods
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    

    
}
