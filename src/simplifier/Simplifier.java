package simplifier;

import java.util.Scanner;
import simplifier.UserDatabase;

/**
 * This class provides a simple user registration and login system.
 * It includes an option for user registration and login, as well as admin login.
 * Users are stored in a UserDatabase.
 *
 * @author Tiago
 */
public class Simplifier {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Welcome to Simplifier!");

            System.out.println("Choose an option:");
            System.out.println("1 - Register");
            System.out.println("2 - Login");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            if (choice == 1) {
                registerUser(scanner);
            } else if (choice == 2) {
                loginUser(scanner);
            }
        }
    }

    private static void registerUser(Scanner scanner) {
        System.out.println("User Registration:");

        System.out.print("Enter your full name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        System.out.print("Enter your gross income: ");
        System.out.print("Enter your tax credits: ");

        RUser regularUser = new RUser(fullName, email, password);
        UserDatabase.registerUser(regularUser);

        System.out.println("User successfully registered!");
    }

    private static void loginUser(Scanner scanner) {
        System.out.println("Login:");

        System.out.print("Digite seu email: ");
        String email = scanner.nextLine();
        System.out.print("Digite sua senha: ");
        String password = scanner.nextLine();

    if (email.equals("CCT") && password.equals("2023")) {
        Admin admin = new Admin("Admin", email, password);
        admin.showAdminMenu();
    } else {
        User regularUser = UserDatabase.getUserByEmailAndPassword(email, password);
        if (regularUser != null && regularUser instanceof RUser) {
            RUser rUser = (RUser) regularUser;
            rUser.showUserMenu(scanner);
        } else {
            System.out.println("Login falhou. Verifique suas credenciais.");
        }
    }
}
}
    

    

