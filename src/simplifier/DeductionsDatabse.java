


package simplifier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Tiago
 */



class DeductionsDatabase {
    public static void addDeduction(RUser rUser, String deductionName, double deductionValue) {
        String url = "jdbc:mysql://localhost:3306/java_simplifier_database";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword)) {
            String query = "INSERT INTO deductions (deduction_name, deduction_value, user_id) VALUES (?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, deductionName);
                preparedStatement.setDouble(2, deductionValue);
                preparedStatement.setInt(3, rUser.getEmail().hashCode());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static double getDeductionsTotal(RUser rUser) {
        double totalDeductions = 0;

        String url = "jdbc:mysql://localhost:3306/java_simplifier_database";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword)) {
            String query = "SELECT SUM(deduction_value) FROM deductions WHERE user_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, rUser.getEmail().hashCode());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        totalDeductions = resultSet.getDouble(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalDeductions;
    }
}
