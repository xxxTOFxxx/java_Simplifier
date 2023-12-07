
package simplifier;

/**
 *
 * @author Tiago
 */
class RUser extends User {
    public RUser(String fullName, String email, String password, String password1) {
    super("", fullName, email, password, 0);  
    // Pass actual values to the superclass constructor
}
    public void showUserMenu() {
        System.out.println("Welcome, User! Available options:");
        // Implement specific options for the Regular User
    }
}


