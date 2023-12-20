
package simplifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import simplifier.UserDatabase;

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
        System.out.println("5 - Import User List from CSV"); // Nova opção
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
            case 5:
                importUserListFromCSV(); // Chamada correta ao método
                break;
            case 0:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    } while (choice != 0);

    scanner.close();
}


    
    public void importUserListFromCSV() {
        System.out.println("Enter the path to the CSV file: ");
        String filePath = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            List<RUser> usersToAdd = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String fullName = data[0].trim();
                    String email = data[1].trim();
                    String password = data[2].trim();

                    RUser newUser = new RUser(fullName, email, password);
                    usersToAdd.add(newUser);
                }
            }

            // Add the users to the database
            for (RUser user : usersToAdd) {
                UserDatabase.createUser(user);
                System.out.println("User added: " + user.getFullName());
            }

            System.out.println(usersToAdd.size() + " users added from the CSV file.");
        } catch (IOException e) {
            System.out.println("Error reading the CSV file: " + e.getMessage());
        }
    }

	@Override
	public void showUserMenu(Scanner scanner) {
		//Tem q implementar esse metodo abstrato. Se for util.
	}
}
