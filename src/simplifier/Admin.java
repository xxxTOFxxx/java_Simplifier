
package simplifier;

/**
 *
 * @author Tiago
 */
class Admin extends User {
    public Admin(String username) {
        super(username, "", "", "", 0);  // Dummy data for Admin
    }

    public void showAdminMenu() {
        System.out.println("Welcome, Admin! Available options:");
        // Implement specific options for the Admin
    }
}
