
/*
 * The UserDatabase class provides methods for connecting to a MySQL database,
 * closing the database connection, and registering a user in the database.
 */

package simplifier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import simplifier.TaxCalculator;

/**
 * Represents a database interaction class for user-related operations.
 * 
 * @author Tiago
 */

public class UserDatabase {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/java_simplifier_database";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "";

    public static void createUser(RUser user) {
    try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
        String insertSQL = "INSERT INTO user (email, full_name, gross_income, tax_credits) VALUES (?, ?, ROUND(?, 2), ROUND(?, 2))";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getFullName());
            pstmt.setDouble(3, user.getGrossIncome());
            pstmt.setDouble(4, user.getTaxCredits());
            pstmt.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    public static User getUser(String email) {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            String selectSQL = "SELECT full_name, email, password, type FROM user WHERE email = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
                pstmt.setString(1, email);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String fullName = rs.getString("full_name");
                        String password = rs.getString("password");
                        String type = rs.getString("type");
                        return new User(fullName, email, password, type);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    static void registerUser(RUser user) {
        try (Connection connection = connect()) {
            String query = "INSERT INTO user (full_name, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, user.getFullName());
                statement.setString(2, user.getEmail());
                statement.setString(3, user.getPassword());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }
    public static boolean doesUserExist(String email) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            String query = "SELECT 1 FROM user WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    

   public static RUser getUserByEmailAndPassword(String email, String password) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            String query = "SELECT * FROM user WHERE email = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, email);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return new RUser(
                                resultSet.getString("full_name"),
                                resultSet.getString("email"),
                                resultSet.getString("password")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return null;
    }
    public static void viewUserList() {
    try (Connection connection = connect()) {
        String query = "SELECT * FROM user";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String fullName = resultSet.getString("full_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                
                System.out.println("ID: " + id + ", Full Name: " + fullName + ", Email: " + email + ", Password: " + password);
            }
        }
    } catch (SQLException e) {
        handleSQLException(e);
    }
}



    public static User selectUser() {
    try (Connection connection = connect()) {
        String query = "SELECT * FROM user";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = new RUser(
                        resultSet.getString("full_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
                users.add(user);
            }

            if (!users.isEmpty()) {
                System.out.println("Select an option:");
                System.out.println("0 - Back to Main Menu");
                for (User user : users) {
                    System.out.println("User ID: " + (users.indexOf(user) + 1) +
                            ", Full Name: " + user.getFullName() +
                            ", Email: " + user.getEmail() +
                            ", Password: " + user.getPassword());
                }

                int selection;
                try {
                    selection = Integer.parseInt(new Scanner(System.in).nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    return null;
                }

                if (selection == 0) {
                    return null; // User chose to go back to the main menu
                } else if (selection >= 1 && selection <= users.size()) {
                    return users.get(selection - 1);
                } else {
                    System.out.println("Invalid selection. Please try again.");
                }
            } else {
                System.out.println("No users available.");
            }
        }
    } catch (SQLException e) {
        handleSQLException(e);
    }
    return null;
}


    public static void editUser(User user) {
        try (Connection connection = connect()) {
            String query = "UPDATE user SET full_name = ?, email = ?, password = ? WHERE email = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter new full name: ");
                statement.setString(1, scanner.nextLine());
                System.out.print("Enter new email: ");
                statement.setString(2, scanner.nextLine());
                System.out.print("Enter new password: ");
                statement.setString(3, scanner.nextLine());
                statement.setString(4, user.getEmail());

                statement.executeUpdate();
                System.out.println("User updated successfully.");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public static void deleteUser(User user) {
        try (Connection connection = connect()) {
            String query = "DELETE FROM user WHERE email = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, user.getEmail());

                statement.executeUpdate();
                System.out.println("User deleted successfully.");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    static void handleSQLException(SQLException e) {
        System.err.println("SQL Exception:");
        System.err.println("Error Message: " + e.getMessage());
        System.err.println("SQL State: " + e.getSQLState());
        System.err.println("Error Code: " + e.getErrorCode());
        e.printStackTrace();
    }
    
      public static void saveTaxCalculation(TaxCalculator taxCalculation) {
        String url = "jdbc:mysql://localhost:3306/java_simplifier"; // Use the correct database URL
        String userDB = "root";
        String password = ""; // Use the correct password

        try (Connection connection = DriverManager.getConnection(url, userDB, password)) {
            String insertSQL = "INSERT INTO tax_calculation (user_id, marital_status, grossIncome, net_income, income_tax, usc, prsi, deduction_name, deductions, calculation_date) "
                                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, taxCalculation.getUserId());
                preparedStatement.setDouble(2, taxCalculation.getIncomeTax());
                preparedStatement.setDouble(3, taxCalculation.getUsc());
                preparedStatement.setDouble(4, taxCalculation.getPrsi());
                preparedStatement.setDouble(5, taxCalculation.getNetIncome());
                preparedStatement.setDouble(6, taxCalculation.getGrossIncome());
                preparedStatement.setString(7, taxCalculation.getDeductionName());
                preparedStatement.setDouble(8, taxCalculation.getDeductionValue());
                preparedStatement.setTimestamp(9, taxCalculation.getCalculationDate());

                preparedStatement.executeUpdate();
                System.out.println("Cálculo de imposto salvo no banco de dados.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<TaxCalculator> getTaxCalculations() {
        List<TaxCalculator> taxCalculations = new ArrayList<>();

        String url = "jdbc:mysql://localhost:3306/tax_calculation"; // Use the correct database URL
        String userDB = "root";
        String password = "password"; // Use the correct password

        try (Connection connection = DriverManager.getConnection(url, userDB, password)) {
            String query = "SELECT * FROM tax_calculation";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int userId = resultSet.getInt("user_id");
                    double incomeTax = resultSet.getDouble("income_tax");
                    double usc = resultSet.getDouble("usc");
                    double prsi = resultSet.getDouble("prsi");
                    double netIncome = resultSet.getDouble("net_income");
                    double grossIncome = resultSet.getDouble("gross_income");
                    String deductionName = resultSet.getString("deduction_name");
                    double deductionValue = resultSet.getDouble("deduction_value");
                    Timestamp calculationDate = resultSet.getTimestamp("calculation_date");

                    TaxCalculator taxCalculator = new TaxCalculator(userId, incomeTax, usc, prsi, netIncome, grossIncome, deductionName, deductionValue, calculationDate);
                    taxCalculations.add(taxCalculator);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return taxCalculations;
    }
}
