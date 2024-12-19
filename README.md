# Car Ownership Calculator

A Spring Boot application that calculates the total cost of car ownership, including monthly installments, insurance costs, and tracker expenses.

## Features

- Calculate monthly car installments
- Calculate total cost of car ownership
- Provide detailed cost breakdown
- Available through both CLI and REST API

## Requirements

- Java 11 or higher
- Maven
- Spring Boot 2.x

## Installation

1. Clone the repository:

```bash
git clone [repository-url]
```

2. Navigate to the project directory:

```bash
cd ownership.calculator
```

3. Build the project:

```bash
mvn clean install
```

## Usage

### Command Line Interface

Run the application using:

```bash
mvn spring-boot:run
```

Follow the prompts to enter:

- Car Price
- Annual Insurance Cost
- Tracker Cost
- Loan Term (in years)
- Annual Interest Rate (as a decimal, e.g., 0.0725 for 7.25%)

### REST API

The application also provides a REST API endpoint:

**Endpoint:** `POST /api/car-ownership/calculate`

**Parameters:**

- `carPrice` (double): The price of the car
- `annualInsuranceCost` (double): Annual insurance cost
- `trackerCost` (double): One-time tracker installation cost
- `loanTermYears` (int): Loan term in years
- `annualInterestRate` (double): Annual interest rate as a decimal

**Example Request:**

```bash
curl -X POST "http://localhost:8080/api/car-ownership/calculate?carPrice=300000&annualInsuranceCost=12000&trackerCost=1500&loanTermYears=5&annualInterestRate=0.0725"
```

## Sample Output

```
--- Ownership Cost Breakdown ---
Car Price: R 300,000.00
Tracker Cost: R 1,500.00
Annual Insurance Cost: R 12,000.00
Loan Term: 5 years
Annual Interest Rate: 7.25%
Monthly Installment: R 5,975.98
Total Cost of Ownership: R 419,558.80
-----------------------------------------------
Payable Amount for Loan Term: R 119,558.80
```

## Error Handling

The application includes validation for:

- Negative values
- Invalid loan terms
- Interest rates outside the valid range (0-1)

## Contributing

Feel free to submit issues and enhancement requests.

## License

This project is licensed under the MIT License.[MIT License](https://opensource.org/licenses/MIT)
