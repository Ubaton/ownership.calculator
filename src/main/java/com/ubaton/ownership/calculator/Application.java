package com.ubaton.ownership.calculator;

import com.ubaton.ownership.calculator.method.CarOwnershipCalculator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Scanner;
import java.text.DecimalFormat;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.ubaton.ownership.calculator",
		"com.ubaton.ownership.calculator.method",
		"com.ubaton.ownership.calculator.controller"
})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(CarOwnershipCalculator calculator) {
		return args -> {
			try (Scanner scanner = new Scanner(System.in)) {
				// User input for car details
				System.out.println("Car Ownership Cost Calculator");
				System.out.println("------------------------------");

				System.out.print("Enter Car Price: R ");
				double carPrice = scanner.nextDouble();

				System.out.print("Enter Annual Insurance Cost: R ");
				double annualInsuranceCost = scanner.nextDouble();

				System.out.print("Enter Tracker Cost: R ");
				double trackerCost = scanner.nextDouble();

				System.out.print("Enter Loan Term (in years): ");
				int loanTermYears = scanner.nextInt();

				System.out.print("Enter Annual Interest Rate (as a decimal, e.g., 0.0725 for 7.25%): ");
				double annualInterestRate = scanner.nextDouble();

				// Get the cost breakdown using the calculator service
				String result = calculator.getCostBreakdown(
						carPrice,
						annualInsuranceCost,
						trackerCost,
						loanTermYears,
						annualInterestRate
				);

				// Display the results
				System.out.println(result);
			} catch (IllegalArgumentException e) {
				System.err.println("Input Error: " + e.getMessage());
			} catch (Exception e) {
				System.err.println("An unexpected error occurred. Please check your inputs and try again.");
			}
		};
	}
}