
package simplifier;

/**
 *
 * @author Tiago
 */
public class TaxClass {
    // Other class attributes
    private int userID;
    private double grossIncome;
    private double taxCredits;
    private static final double PAYETaxRate = 0.2;

    // Class constructor
    public TaxClass(/* Other parameters */, int userID, double grossIncome, double taxCredits) {
        // Initialize other attributes...
        this.userID = userID;
        this.grossIncome = grossIncome;
        this.taxCredits = taxCredits;

        
        calculateTaxes();
    }

    // Methods for specific tax calculations
    private double calculatePAYE(double grossIncome) {
        
        double personalCredits = 

        
        double specificDeductions = 

        
        double paye = (grossIncome - specificDeductions) * PAYETaxRate - personalCredits;

        return paye;
    }

    private double calculateUSC(double grossIncome) {
        
        double USCrate = 

        
        double usc = grossIncome * USCrate;

        return usc;
    }

    private void calculateTaxes() {
        
        double paye = calculatePAYE(grossIncome);
        double usc = calculateUSC(grossIncome);
        double prsi = calculatePRSI(grossIncome);

        // Sum the calculated values
        double totalTax = paye + usc + prsi;

        // Subtract Tax Credits
        double finalTax = totalTax - taxCredits;

        // Display the Result (or do whatever you want with the result)
        System.out.println("The amount of tax due is: " + finalTax);
    }

    
    private double calculatePRSI(double grossIncome) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
