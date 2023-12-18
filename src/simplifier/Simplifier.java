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

            int choice = getChoice(scanner);

            switch (choice) {
                case 1:
                    registerUser(scanner);
                    break;
                case 2:
                    loginUser(scanner);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    private static int getChoice(Scanner scanner) {
        System.out.println("Choose an option:");
        System.out.println("1 - Register");
        System.out.println("2 - Login");
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine(); // clear buffer
            return -1;
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

    User regularUser = UserDatabase.getUserByEmailAndPassword(email, password);

    if (regularUser != null && regularUser instanceof RUser) {
        RUser rUser = (RUser) regularUser;

        
        if (UserDatabase.doesUserExist(rUser.getEmail())) {
            rUser.showUserMenu(scanner);
        } else {
            System.out.println("Usuário não encontrado.");
        }
    } else {
        System.out.println("Login falhou. Verifique suas credenciais.");
    }
}
}
