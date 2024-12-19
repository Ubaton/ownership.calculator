package com.ubaton.ownership.calculator.method;

import org.springframework.stereotype.Service;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CarOwnershipCalculator {
    private static final Logger LOGGER = Logger.getLogger(CarOwnershipCalculator.class.getName());
    private final DecimalFormat df = new DecimalFormat("#,###.00");

    // Method to calculate monthly installment
    public double calculateMonthlyInstallment(double carPrice, int loanTermYears, double annualInterestRate) {
        double monthlyInterestRate = annualInterestRate / 12;
        int numberOfPayments = loanTermYears * 12;

        return carPrice * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfPayments))
                / (Math.pow(1 + monthlyInterestRate, numberOfPayments) - 1);
    }

    // Method to calculate total cost of ownership
    public double calculateTotalCostOfOwnership(
            double carPrice,
            double annualInsuranceCost,
            double trackerCost,
            int loanTermYears,
            double annualInterestRate
    ) {
        validateInputs(carPrice, annualInsuranceCost, trackerCost, loanTermYears, annualInterestRate);

        double totalLoanAmount = carPrice + trackerCost;
        double monthlyInstallment = calculateMonthlyInstallment(totalLoanAmount, loanTermYears, annualInterestRate);
        double totalInterestPaid = (monthlyInstallment * loanTermYears * 12) - totalLoanAmount;
        double totalInsuranceCost = annualInsuranceCost * loanTermYears;

        return totalLoanAmount + totalInterestPaid + totalInsuranceCost;
    }

    // Method to get formatted cost breakdown
    public String getCostBreakdown(
            double carPrice,
            double annualInsuranceCost,
            double trackerCost,
            int loanTermYears,
            double annualInterestRate
    ) {
        try {
            double monthlyInstallment = calculateMonthlyInstallment(carPrice, loanTermYears, annualInterestRate);
            double totalCostOfOwnership = calculateTotalCostOfOwnership(
                    carPrice,
                    annualInsuranceCost,
                    trackerCost,
                    loanTermYears,
                    annualInterestRate
            );

            String breakdown = "\n--- Ownership Cost Breakdown ---\n" +
                    "Car Price: R " + df.format(carPrice) + "\n" +
                    "Tracker Cost: R " + df.format(trackerCost) + "\n" +
                    "Annual Insurance Cost: R " + df.format(annualInsuranceCost) + "\n" +
                    "Loan Term: " + loanTermYears + " years\n" +
                    "Annual Interest Rate: " + (annualInterestRate * 100) + "%\n" +
                    "Monthly Installment: R " + df.format(monthlyInstallment) + "\n" +
                    "Total Cost of Ownership: R " + df.format(totalCostOfOwnership);

            LOGGER.info("Car ownership cost calculation completed successfully");
            return breakdown;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error calculating cost breakdown", e);
            throw e;
        }
    }

    private void validateInputs(double carPrice, double annualInsuranceCost, double trackerCost,
                                int loanTermYears, double annualInterestRate) {
        if (carPrice < 0 || annualInsuranceCost < 0 || trackerCost < 0 || loanTermYears <= 0
                || annualInterestRate <= 0 || annualInterestRate > 1) {
            throw new IllegalArgumentException("All input values must be positive, loan term must be greater than zero, " +
                    "and interest rate must be between 0 and 1 (e.g., 0.0725 for 7.25%).");
        }
    }
}