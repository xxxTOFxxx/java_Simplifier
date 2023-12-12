
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

    // Additional methods...

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
        scanner.nextLine(); // Consume the invalid input
    }

    public void initiateTaxCalculation() {
        System.out.println("Tax calculation initiated. Implement the logic here.");
    }

    @Override
    public void showUserMenu() {
        super.showUserMenu();
        System.out.println("3 - Edit Profile");
        System.out.println("4 - Initiate Tax Calculation");

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
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (java.util.InputMismatchException e) {
            handleInputMismatch();
        }
    }
}
