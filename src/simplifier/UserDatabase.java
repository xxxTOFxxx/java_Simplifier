
/*
 * The UserDatabase class provides methods for connecting to a MySQL database,
 * closing the database connection, and registering a user in the database.
 */

package simplifier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Represents a database interaction class for user-related operations.
 * 
 * @author Tiago
 */
class UserDatabase {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/java_simplifier_user_database";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    /**
     * Establishes a connection to the database.
     * 
     * @return Connection object representing the database connection.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Closes the provided database connection.
     * 
     * @param connection Connection object to be closed.
     * @throws SQLException If a database access error occurs.
     */
    public static void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Registers a user in the database.
     * 
     * @param user User object containing user details to be registered.
     */
    public static void registerUser(User user) {
    try (Connection connection = connect()) {
        // SQL query to insert user data into the 'users' table
        String query = "INSERT INTO user (full_name, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            System.out.println("SQL Query: " + preparedStatement.toString());

            // Set values for the placeholders in the SQL query
            preparedStatement.setString(1, user.getFullName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());

            // Execute the SQL update statement
            preparedStatement.executeUpdate();
        }
    } catch (SQLException e) {
        // Print stack trace in case of a database error
        e.printStackTrace();
    }
}

    

    static RUser getUserByEmailAndPassword(String usernameOrEmail, String password) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

