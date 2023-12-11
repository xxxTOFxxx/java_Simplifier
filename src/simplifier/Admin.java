
package simplifier;

import java.util.Scanner;

/**
 *
 * @author Tiago
 */
class Admin extends User {
    private static Scanner scanner = new Scanner(System.in);
    public Admin(String username) {
        super(username, "", "", "", 0);  // Dummy data for Admin
    }

    public void showAdminMenu() {
        
        int choice;
        do {
        System.out.println("Welcome, Admin! Available options:");
        System.out.println("1 - View User List");
        System.out.println("2 - Select User");
        System.out.println("3 - Edit User");
        System.out.println("4 - Delete User");
        System.out.println("0 - logout");
        
        choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    openUserList();
                    break;
                case 2:
                    selectUser();
                    break;
                case 3:
                    editUser();
                    break;
                case 4:
                    deleteUser();
                    break;
                case 0:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
    }while (choice != 0);
}

    private void openUserList() {
        // Implement logic to open the list of users
        System.out.println("Opening user list...");
    }

    private void selectUser() {
        // Implement logic to select a user from the list
        System.out.println("Selecting user...");
    }

    private void editUser() {
        // Implement logic to edit a selected user
        System.out.println("Editing user...");
    }

    private void deleteUser() {
        // Implement logic to delete a selected user
        System.out.println("Deleting user...");
    }
}

