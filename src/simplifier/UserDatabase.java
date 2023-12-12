
/*
 * The UserDatabase class provides methods for connecting to a MySQL database,
 * closing the database connection, and registering a user in the database.
 */

package simplifier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents a database interaction class for user-related operations.
 * 
 * @author Tiago
 */

class UserDatabase {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/java_simplifier_user_database";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Simulated database connection method
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void registerUser(User user) {
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

    public static User getUserByEmailAndPassword(String email, String password) {
        try (Connection connection = connect()) {
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

    private static void handleSQLException(SQLException e) {
        System.err.println("SQL Exception:");
        System.err.println("Error Message: " + e.getMessage());
        System.err.println("SQL State: " + e.getSQLState());
        System.err.println("Error Code: " + e.getErrorCode());
        e.printStackTrace(); 
    }
}