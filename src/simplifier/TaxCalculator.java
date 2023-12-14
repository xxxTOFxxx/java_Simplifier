package simplifier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import simplifier.UserDatabase;

/**
 *
 * @author Tiago
 */
public class TaxCalculator {
    private int userID;
    private double grossIncome;
    private double taxCredits;
    private static final double PAYE_TAX_RATE = 0.2;
    private static final double USC_RATE = 0.07;
    private static final double PRSI_RATE = 0.04;
    private static final double PERSONAL_CREDITS = 2000;

    public TaxCalculator(RUser user) {
        this.userID = user.getEmail().hashCode();
        this.grossIncome = user.getGrossIncome();
        this.taxCredits = user.getTaxCredits();

        calculateTaxes(user);
    }

    private double calculatePAYE(double grossIncome) {
        double specificDeductions = 0;
        return (grossIncome - specificDeductions) * PAYE_TAX_RATE - PERSONAL_CREDITS;
    }

    private double calculateUSC(double grossIncome) {
        return grossIncome * USC_RATE;
    }

    private double calculatePRSI(double grossIncome) {
        return grossIncome * PRSI_RATE;
    }

    private void calculateTaxes(RUser user) {
        double paye = calculatePAYE(grossIncome);
        double usc = calculateUSC(grossIncome);
        double prsi = calculatePRSI(grossIncome);

        double totalTax = paye + usc + prsi;
        double finalTax = totalTax - taxCredits;

        System.out.println("Tax calculation for " + user.getFullName());
        System.out.println("PAYE: " + paye);
        System.out.println("USC: " + usc);
        System.out.println("PRSI: " + prsi);
        System.out.println("Total Tax Due: " + finalTax);

        storeTaxInfo(user, paye, usc, prsi, finalTax);
    }

   private void storeTaxInfo(RUser user, double paye, double usc, double prsi, double finalTax) {
    try (Connection connection = UserDatabase.connect()) {
        String query = "INSERT INTO tax_calculation (user_id, paye, usc, prsi, final_tax, calculation_date) VALUES (?, ?, ?, ?, ?, NOW())";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getEmail().hashCode());
            statement.setDouble(2, paye);
            statement.setDouble(3, usc);
            statement.setDouble(4, prsi);
            statement.setDouble(5, finalTax);
            statement.executeUpdate();
        }
            } catch (SQLException e) {
            UserDatabase.handleSQLException(e);
    }

    }
}