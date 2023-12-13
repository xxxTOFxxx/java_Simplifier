
package simplifier;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Tiago
 */

class Admin extends User {
    private static Scanner scanner = new Scanner(System.in);
    private static final String ADMIN_PASSWORD = "2023";

    public Admin(String fullName, String email, String password) {
        super(fullName, email, password);
    }

    public void showAdminMenu() {
    int choice = -1; 
    do {
        System.out.println("Welcome, Admin! Available options:");
        System.out.println("1 - View User List");
        System.out.println("2 - Select User");
        System.out.println("3 - Edit User");
        System.out.println("4 - Delete User");
        System.out.println("0 - Logout");

        try {
        choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException | InputMismatchException e) {
             System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine(); // Consume the invalid input
            continue; 
        }


        switch (choice) {
            case 1:
                UserDatabase.viewUserList();
                break;
            case 2:
                User selectedUser = UserDatabase.selectUser();
                if (selectedUser != null) {
                    System.out.println("Selected user: " + selectedUser.getFullName());
                } else {
                    System.out.println("User not found.");
                }
                break;
            case 3:
                User selectedEditUser = UserDatabase.selectUser();
                if (selectedEditUser != null) {
                    UserDatabase.editUser(selectedEditUser);
                } else {
                    System.out.println("User not found.");
                }
                break;
            case 4:
                User selectedDeleteUser = UserDatabase.selectUser();
                if (selectedDeleteUser != null) {
                    UserDatabase.deleteUser(selectedDeleteUser);
                } else {
                    System.out.println("User not found.");
                }
                break;
            case 0:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    } while (choice != 0); // Continue the loop until the user chooses to logout

    scanner.close();
    }
    
}