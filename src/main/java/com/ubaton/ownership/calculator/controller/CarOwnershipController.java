package com.ubaton.ownership.calculator.controller;

import com.ubaton.ownership.calculator.method.CarOwnershipCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/car-ownership")
public class CarOwnershipController {

    private final CarOwnershipCalculator calculator;
    private static final Logger LOGGER = Logger.getLogger(CarOwnershipController.class.getName());

    @Autowired
    public CarOwnershipController(CarOwnershipCalculator calculator) {
        this.calculator = calculator;
    }

    @PostMapping("/calculate")
    public ResponseEntity<String> calculateOwnership(
            @RequestParam double carPrice,
            @RequestParam double annualInsuranceCost,
            @RequestParam double trackerCost,
            @RequestParam int loanTermYears,
            @RequestParam double annualInterestRate) {

        try {
            String breakdown = calculator.getCostBreakdown(
                    carPrice,
                    annualInsuranceCost,
                    trackerCost,
                    loanTermYears,
                    annualInterestRate
            );
            return ResponseEntity.ok(breakdown);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Input Error: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error occurred while calculating ownership", e);
            return ResponseEntity.internalServerError().body("An unexpected error occurred. Please check your inputs and try again.");
        }
    }
}