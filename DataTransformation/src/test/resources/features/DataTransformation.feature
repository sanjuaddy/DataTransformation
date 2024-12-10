Feature: Data Transformation

  Scenario: Validate PositionReport.csv generation
    Given the input files "InstrumentDetails.csv" and "PositionDetails.csv" exist in the "app/in" directory
    When the application processes the input files
    Then the output file "PositionReport.csv" should be generated in the "app/out" directory
    And the data in "PositionReport.csv" should match the expected transformation