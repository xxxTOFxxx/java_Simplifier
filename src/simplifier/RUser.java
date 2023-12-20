package simplifier;

import java.util.InputMismatchException;
import java.util.Scanner;


/**
 *
 * @author Tiago
 */

public class RUser extends User {
    private double grossIncome;
    private double taxCredits;
    private Scanner scanner;

    public RUser(String fullName, String email, String password) {
        super(fullName, email, password);
        this.grossIncome = 0.0; // Inicializa com valor padrão
        this.taxCredits = 0.0; // Inicializa com valor padrão
        this.scanner = new Scanner(System.in);
    }

    public double getGrossIncome() {
        return grossIncome;
    }

    public void setGrossIncome(double grossIncome) {
        this.grossIncome = grossIncome;
    }

    public double getTaxCredits() {
        return taxCredits;
    }

    public void setTaxCredits(double taxCredits) {
        this.taxCredits = taxCredits;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void editProfile() {
        try {
            System.out.print("Enter new gross income: ");
            double newGrossIncome = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            if (newGrossIncome > 0) {
                setGrossIncome(newGrossIncome);
                System.out.print("Enter new tax credits: ");
                setTaxCredits(scanner.nextDouble());
                scanner.nextLine(); // Consume newline
                System.out.println("Profile updated successfully.");
            } else {
                System.out.println("Invalid gross income. Please enter a positive number.");
            }
        } catch (InputMismatchException e) {
            handleInputMismatch();
        }
    }

    private void handleInputMismatch() {
        System.out.println("Invalid input. Please enter a valid number.");
        scanner.nextLine(); // Consume newline
    }

    public void initiateTaxCalculation() {
        System.out.println("Tax calculation initiated.");
        new TaxCalculator(this);
    }

    @Override
    public void showUserMenu(Scanner scanner1) {
        System.out.println("3 - Editar Perfil");
        System.out.println("4 - Iniciar Cálculo de Imposto");

        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 3:
                    editProfile();
                    break;
                case 4:
                    initiateTaxCalculation();
                    break;
                default:
                    System.out.println("Escolha inválida. Por favor, tente novamente.");
            }
        } catch (InputMismatchException e) {
            handleInputMismatch();
        }
    }
}
