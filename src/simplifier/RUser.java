
package simplifier;

import java.util.Scanner;



/**
 *
 * @author Tiago
 */
 class RUser extends User {
    private int age;
    private String gender;
    private String profession;
    private Scanner scanner;

    // Constructors

    public RUser(String fullName, String email, String password) {
        super(fullName, email, password);
        this.scanner = new Scanner(System.in);
    }

    public RUser(String fullName, String email, String password, int age, String gender, String profession) {
        super(fullName, email, password);
        this.age = age;
        this.gender = gender;
        this.profession = profession;
    }

    // Getters and setters...

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void editProfile() {
        try {
            System.out.print("Enter new age: ");
            int newAge = scanner.nextInt();
            if (newAge > 0) {
                setAge(newAge);
                scanner.nextLine();
                System.out.print("Enter new gender: ");
                setGender(scanner.nextLine());
                System.out.print("Enter new profession: ");
                setProfession(scanner.nextLine());
                System.out.println("Profile updated successfully.");
            } else {
                System.out.println("Invalid age. Please enter a positive number.");
            }
        } catch (java.util.InputMismatchException e) {
            handleInputMismatch();
        }
    }

    private void handleInputMismatch() {
        System.out.println("Invalid input. Please enter a valid number.");
        scanner.nextLine(); 
    }

    public void initiateTaxCalculation() {
        System.out.println("Tax calculation initiated. Implement the logic here.");
    }

    
    public void showUserMenu() {
        
        System.out.println("3 - Editar Perfil");
        System.out.println("4 - Iniciar Cálculo de Imposto");

        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
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
        } catch (java.util.InputMismatchException e) {
            handleInputMismatch();
        }
    }
    
}


