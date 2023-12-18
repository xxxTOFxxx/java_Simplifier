package simplifier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;
import java.sql.ResultSet;



/**
 *
 * @author Tiago
 */

public class TaxCalculator {
    private int userID;
    private double grossIncome;
    private double taxCredits;
    private double netIncome;
    private double totalIncomeTaxPaid;

    public TaxCalculator(RUser user) {
        this.userID = user.getEmail().hashCode();
        this.grossIncome = user.getGrossIncome();
        this.taxCredits = user.getTaxCredits();
        this.totalIncomeTaxPaid = retrieveTotalIncomeTaxPaid(); // Retrieve total income tax paid from the database

        calculateTaxes(user);
    }
   
    private void calculateTaxes(RUser user) {
        try (Scanner scanner = new Scanner(System.in)){

        System.out.print("Enter marital status (single or married): ");
        String maritalStatus = scanner.next();

        System.out.print("Enter gross income: ");
        double grossIncome = scanner.nextDouble();

        System.out.print("Enter income frequency (weekly, bi-weekly, or monthly): ");
        String incomeFrequency = scanner.next();

        System.out.print("Enter additional deductions (or 0 if none): ");
         double deductions = DeductionsDatabase.getDeductionsTotal(user);


        double netIncome = calculateNetIncome(maritalStatus, grossIncome, incomeFrequency, deductions);

        double incomeTax = calculateIncomeTax(grossIncome, maritalStatus) / getPeriodsPerYear(incomeFrequency);
        double usc = calculateUSC(grossIncome, maritalStatus) / getPeriodsPerYear(incomeFrequency);
        double prsi = calculatePRSI(grossIncome, maritalStatus) / getPeriodsPerYear(incomeFrequency);
        double totalTaxesPaid = incomeTax + usc + prsi;
        
        System.out.println("Income Tax: " + String.format("%.2f", incomeTax));
        System.out.println("USC: " + String.format("%.2f", usc));
        System.out.println("PRSI: " + String.format("%.2f", prsi));
        System.out.println("Net Income: " + String.format("%.2f", netIncome));
        System.out.println("Total Taxes Paid: " + String.format("%.2f", totalTaxesPaid));
        System.out.println("Total Income Tax Paid: " + String.format("%.2f", totalIncomeTaxPaid));
        
        
        saveToDatabase(maritalStatus, grossIncome, netIncome, incomeTax, usc, prsi);


    }
}
    
    private double calculateIncomeTax(double income, String maritalStatus) {
        double taxableIncome = income - taxCredits;
        if (maritalStatus.equalsIgnoreCase("single")) {
            if (income <= 35300) {
                return income * 0.20;
            } else if (income <= 70045) {
                return (income - 35300) * 0.40 + 35300 * 0.20;
            } else {
                return (income - 70045) * 0.45 + 35300 * 0.40 + 35300 * 0.20;
            }
        } else if (maritalStatus.equalsIgnoreCase("married")) {
            if (income <= 44300) {
                return income * 0.20;
            } else if (income <= 70045) {
                return (income - 44300) * 0.40 + 44300 * 0.20;
            } else {
                return (income - 70045) * 0.45 + 44300 * 0.40 + 44300 * 0.20;
            }
        } else {
            System.out.println("Invalid marital status. Use 'single' or 'married'.");
            System.exit(1);
            return 0;
        }
    }
    
    private int getPeriodsPerYear(String frequency) {
    switch (frequency.toLowerCase()) {
        case "weekly":
            return 52;
        case "bi-weekly":
            return 26;
        case "monthly":
            return 12;
        default:
            System.out.println("Invalid frequency. Use 'weekly', 'bi-weekly', or 'monthly'.");
            System.exit(1);
            return 0;
    }
}

    private double calculateNetIncome(String maritalStatus, double grossIncome, String frequency, double deductions) {
        int periodsPerYear = getPeriodsPerYear(frequency);
            double annualIncome = grossIncome * periodsPerYear;

            double incomeTax = calculateIncomeTax(annualIncome, maritalStatus);
            double usc = calculateUSC(annualIncome, maritalStatus);
            double prsi = calculatePRSI(annualIncome, maritalStatus);

            double netIncome = annualIncome - (incomeTax + usc + prsi + deductions);
    
    
             incomeTax /= periodsPerYear;
             usc /= periodsPerYear;
             prsi /= periodsPerYear;
             netIncome /= periodsPerYear;

            return netIncome;
    }



    private double calculateUSC(double annualIncome, String maritalStatus) {
        double usc = 0;

        if (maritalStatus.equalsIgnoreCase("single")) {
            if (annualIncome <= 20484) {
                usc = annualIncome * 0.02;
            } else if (annualIncome <= 70044) {
                usc = (annualIncome - 20484) * 0.045 + 20484 * 0.02;
            } else {
                usc = (annualIncome - 70044) * 0.08 + 70044 * 0.045 + 20484 * 0.02;
            }
        } else if (maritalStatus.equalsIgnoreCase("married")) {
            double combinedIncome = annualIncome * 2;
            if (combinedIncome <= 24024) {
                usc = combinedIncome * 0.005;
            } else if (combinedIncome <= 40968) {
                usc = (combinedIncome - 24024) * 0.02 + 24024 * 0.005;
            } else if (combinedIncome <= 140088) {
                usc = (combinedIncome - 40968) * 0.045 + 40968 * 0.02 + 24024 * 0.005;
            } else {
                usc = (combinedIncome - 140088) * 0.08 + 140088 * 0.045 + 40968 * 0.02 + 24024 * 0.005;
            }
        } else {
            System.out.println("Invalid marital status. Use 'single' or 'married'.");
            System.exit(1);
        }

        return usc;
    }

    private double calculatePRSI(double annualIncome, String maritalStatus) {
        double prsi = 0;

        if (maritalStatus.equalsIgnoreCase("married")) {
            prsi = annualIncome * 0.04; // Assumes the same rates for both categories
        } else if (maritalStatus.equalsIgnoreCase("single")) {
            prsi = annualIncome * 0.04; // Assumes the same rates for both categories
        } else {
            System.out.println("Invalid marital status. Use 'single' or 'married'.");
            System.exit(1);
        }

        return prsi;
    }

    private void saveToDatabase(String maritalStatus, double grossIncome, double netIncome, double incomeTax, double usc, double prsi) {
    String url = "jdbc:mysql://localhost:3306/java_simplifier_database";
    String user = "root";
    String password = "";

    try (Connection connection = DriverManager.getConnection(url, user, password)) {
        // Verifique se o usuário existe na tabela 'user'
        int userId = userID;
        if (!doesUserExist(connection, userId)) {
            // Se o usuário não existir, adicione-o à tabela 'user'
            addUserToDatabase(connection, userId);
        }

        String query = "INSERT INTO tax_calculation (user_id, income_tax, usc, prsi, net_income, grossIncome, final_tax, calculation_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setDouble(2, incomeTax);
            preparedStatement.setDouble(3, usc);
            preparedStatement.setDouble(4, prsi);
            preparedStatement.setDouble(5, netIncome);
            preparedStatement.setDouble(6, grossIncome);

            // Adicione o índice correto para final_tax
                double finalTax = incomeTax + usc + prsi;
                    preparedStatement.setDouble(7, finalTax);

                    Date currentDate = new Date();
                    Timestamp timestamp = new Timestamp(currentDate.getTime());
                    preparedStatement.setTimestamp(8, timestamp);

                     preparedStatement.executeUpdate();
                    System.out.println("Information saved to the database.");
                }
            } catch (SQLException e) {
                e.printStackTrace(); 
            }
        }

            private boolean doesUserExist(Connection connection, int userId) throws SQLException {
                String query = "SELECT 1 FROM user WHERE id = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                       preparedStatement.setInt(1, userId);
                            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                                return resultSet.next();
                             }
                         }
                    }

            private void addUserToDatabase(Connection connection, int userId) throws SQLException {
                    // Adicione aqui a lógica para adicionar o usuário à tabela 'user'
                    // Você pode precisar coletar as informações do usuário antes de adicionar
            }


    private double retrieveTotalIncomeTaxPaid() {
    double totalTaxPaid = 0;

    // Retrieve total income tax paid from the database
    String url = "jdbc:mysql://localhost:3306/java_simplifier_database";
    String user = "root";
    String password = "";

    try (Connection connection = DriverManager.getConnection(url, user, password)) {
        String query = "SELECT SUM(income_tax) FROM tax_calculation WHERE user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Obtenha o total anterior do banco de dados
                    double previousTotalTaxPaid = resultSet.getDouble(1);
                    
                    // Some o total anterior com o total calculado no construtor
                    totalTaxPaid = previousTotalTaxPaid + totalIncomeTaxPaid;
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Considere registrar a exceção ou fornecer uma mensagem mais significativa.
    }

    return totalTaxPaid;
}
}