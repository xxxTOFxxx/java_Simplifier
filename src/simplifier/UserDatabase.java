
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

/**
 * Represents a database interaction class for user-related operations.
 * 
 * @author Tiago
 */

class UserDatabase {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/java_simplifier_user_database";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Establishes a connection to the database
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Closes the provided database connection
    public static void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    // Registers a user in the database
    public static void registerUser(User user) {
        try (Connection connection = connect()) {
            String query = "INSERT INTO user (full_name, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                System.out.println("SQL Query: " + preparedStatement.toString());

                preparedStatement.setString(1, user.getFullName());
                preparedStatement.setString(2, user.getEmail());
                preparedStatement.setString(3, user.getPassword());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieves a user from the database based on email and password
    public static RUser getUserByEmailAndPassword(String email, String password) {
        try (Connection connection = connect()) {
            String query = "SELECT * FROM user WHERE email = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return new RUser(
                            resultSet.getString("full_name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getInt("user_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}