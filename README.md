# Appium Automation with Allure Report

This project is about testing mobile application using `Appium` automation tool.

Key feature:
- POM design pattern.
- Used `excel` file for external data.
- Allure Report for test results.

## Setup & Running Tests

1. Open the project in any IDE & open Android emulator.
2. Run the test: ```mvn test``` or manually run this test class under `src/test/java/com/test`
3. For Allure report(After running tests): ```allure serve```

NB - If you get any error like `directory not found....` while running `allure serve` create a folder under `target` named `allure-results` or `allure-reports` or vice-versa. After that run again the `allure serve` command.

Happy Testing :)

