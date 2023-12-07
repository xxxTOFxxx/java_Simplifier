package simplifier;

import java.util.Scanner;

/**
 * This 1class provides a simple user registration and login system.
 * It includes an option for user registration and login, as well as admin login.
 * Users are stored in a UserDatabase.
 *
 * @author Tiago
 */
public class Simplifier {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Simplifier!");

        // Registration or login option
        System.out.println("Choose an option:");
        System.out.println("1 - Register");
        System.out.println("2 - Login");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        if (choice == 1) {
            registerUser(scanner);
        } else if (choice == 2) {
            loginUser(scanner);
        }
    }

    /**
     * Handles the registration of a new user.
     *
     * @param scanner Scanner object for user input
     */
    private static void registerUser(Scanner scanner) {
        System.out.println("User Registration:");

        System.out.print("Enter your full name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        // Automated ID generation (can be done more robustly)
        //int userId = generateUserId();

        RUser regularUser = new RUser("", fullName, email, password);
        UserDatabase.registerUser(regularUser);

        System.out.println("User successfully registered!");
    }

    /**
     * Handles the login process for both regular users and admin...
     *
     * @param scanner Scanner object for user input
     */
    private static void loginUser(Scanner scanner) {
        System.out.println("Login:");

        System.out.print("Enter your username or email: ");
        String usernameOrEmail = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (usernameOrEmail.equals("CCT") && password.equals("Dublin")) {
            // Admin login
            Admin admin = new Admin(usernameOrEmail);
            admin.showAdminMenu(scanner);
        } else {
            // Regular user login
            RUser regularUser = UserDatabase.getUserByEmailAndPassword(usernameOrEmail, password);
            if (regularUser != null) {
                regularUser.showUserMenu();
            } else {
                System.out.println("Login failed. Check your credentials.");
            }
        }
    }
}

