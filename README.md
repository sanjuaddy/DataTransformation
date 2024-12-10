Data Load and Transformation Test Suite


Setup Steps
Install Java 11+ and Maven.
Clone this repository: git clone <repo-url>
Navigate to the project directory: cd data-load-transformation


Running Tests
Place input files in the app/in directory.
Run tests: mvn test
View reports: Open target/cucumber-reports.html in a browser.

Reporting
The report includes:
Feature Execution Summary: Pass/fail statistics for all scenarios.
Step Details: Logs and validation details for each step.
