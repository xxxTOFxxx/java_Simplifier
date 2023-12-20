package simplifier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;
import java.sql.ResultSet;
import java.text.DecimalFormat;

/**
 * @author Tiago
 */

public class TaxCalculator {
    private int userID;
    private double grossIncome;
    private double taxCredits;
    private double totalIncomeTaxPaid;
    private String userEmail;
    private String fullName;

    public TaxCalculator(RUser user) {
        this.userID = getUserIdByEmail(user.getEmail()); // Updated to use getUserIdByEmail method
        this.grossIncome = user.getGrossIncome();
        this.taxCredits = user.getTaxCredits();
        this.totalIncomeTaxPaid = retrieveTotalIncomeTaxPaid();
        this.userEmail = user.getEmail();
        this.fullName = user.getFullName();

        calculateTaxes();
    }

    private void calculateTaxes() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter marital status (single or married): ");
            String maritalStatus = scanner.next();

            System.out.print("Enter income frequency (weekly, bi-weekly, or monthly): ");
            String incomeFrequency = scanner.next();

            System.out.print("Enter additional deductions (or 0 if none): ");
            double deductions = scanner.nextDouble();

            System.out.print("Enter deduction name (or 'none' if no name): ");
            String deductionName = scanner.next();

            System.out.print("Enter gross income: ");
            double grossIncome = scanner.nextDouble();

            double netIncome = calculateNetIncome(maritalStatus, grossIncome, incomeFrequency, deductions);

            double incomeTax = calculateIncomeTax(grossIncome, maritalStatus) / getPeriodsPerYear(incomeFrequency);
            double usc = calculateUSC(grossIncome, maritalStatus) / getPeriodsPerYear(incomeFrequency);
            double prsi = calculatePRSI(grossIncome, maritalStatus) / getPeriodsPerYear(incomeFrequency);
            double totalTaxesPaid = incomeTax + usc + prsi;

            DecimalFormat df = new DecimalFormat("#.##");
            System.out.println("Income Tax: " + df.format(incomeTax));
            System.out.println("USC: " + df.format(usc));
            System.out.println("PRSI: " + df.format(prsi));
            System.out.println("Net Income: " + df.format(netIncome));
            System.out.println("Total Taxes Paid: " + df.format(totalTaxesPaid));
            System.out.println("Total Income Tax Paid: " + df.format(totalIncomeTaxPaid));


            saveToDatabase(maritalStatus, grossIncome, netIncome, incomeTax, usc, prsi, deductionName, deductions);
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
            prsi = annualIncome * 0.04; 
        } else if (maritalStatus.equalsIgnoreCase("single")) {
            prsi = annualIncome * 0.04; 
        } else {
            System.out.println("Invalid marital status. Use 'single' or 'married'.");
            System.exit(1);
        }

        return prsi;
    }

   private void saveToDatabase(String maritalStatus, double grossIncome, double netIncome, double incomeTax,
        double usc, double prsi, String deductionName, double deductionValue) {
    Connection conn = null;
    PreparedStatement pstmt = null;

    try {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_simplifier_database", "root", "");

        String insertSQL = "INSERT INTO tax_calculation (user_id, marital_status, grossIncome, net_income, income_tax, usc, prsi, deduction_name, deduction_value, calculation_date, final_tax) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        pstmt = conn.prepareStatement(insertSQL);

        pstmt.setInt(1, userID);
        pstmt.setString(2, maritalStatus);
        pstmt.setDouble(3, grossIncome);
        pstmt.setDouble(4, netIncome);
        pstmt.setDouble(5, incomeTax);
        pstmt.setDouble(6, usc);
        pstmt.setDouble(7, prsi);
        pstmt.setString(8, deductionName);
        pstmt.setDouble(9, deductionValue);
        pstmt.setTimestamp(10, new Timestamp(new Date().getTime()));

        // Calculate and set the final_tax value as the sum of IncomeTax, USC, and PRSI
        double finalTax = incomeTax + usc + prsi;
        pstmt.setDouble(11, finalTax);

        pstmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}




    private double retrieveTotalIncomeTaxPaid() {
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
        double totalIncomeTaxPaid = 0;

             try {
              conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_simplifier_database", "root", "");

                 String selectSQL = "SELECT SUM(income_tax) FROM tax_calculation WHERE user_id = ?";
                  pstmt = conn.prepareStatement(selectSQL);
                     pstmt.setInt(1, userID);
                         rs = pstmt.executeQuery();

                if (rs.next()) {
                    totalIncomeTaxPaid = rs.getDouble(1);
                }
                 } catch (SQLException e) {
                     e.printStackTrace();
                        } finally {
                    try {
                     if (rs != null) {
                           rs.close();
                         }
                     if (pstmt != null) {
                          pstmt.close();
                         }
                         if (conn != null) {
                             conn.close();
                       }
                         } catch (SQLException e) {
                            e.printStackTrace();
                         }
                         }

                          return totalIncomeTaxPaid;
        }

    
    private int getUserIdByEmail(String email) {
        String url = "jdbc:mysql://localhost:3306/java_simplifier_database";
        String userDB = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, userDB, password)) {
            String query = "SELECT id FROM user WHERE email = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Return a default value if the user ID is not found
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

    private void addUserToDatabase(Connection connection, String fullName, String email) {
        String query = "INSERT INTO user (email, full_name) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, fullName);
            preparedStatement.executeUpdate();
            System.out.println("User added to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add user to the database.");
        }
    }
}
