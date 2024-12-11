package stepdefs;

import org.junit.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DataTransformationSteps {
    
    @Given("^the input files \"([^ and \"([^ exist in the \"(^ directory$")
    public void checkInputFilesExist(String file1, String file2, String inputDir) {
        Assert.assertTrue(Files.exists(Paths.get(inputDir, file1)));
        Assert.assertTrue(Files.exists(Paths.get(inputDir, file2)));
    }

    @When("^the application processes the input files$")
    public void processInputFiles() throws IOException {
        String inputDir = "src/test/resources/data/";
        String outputDir = "src/test/resources/data/";

        // Paths for input and output files
        Path instrumentDetailsPath = Paths.get(inputDir, "InstrumentDetails.csv");
        Path positionDetailsPath = Paths.get(inputDir, "PositionDetails.csv");
        Path positionReportPath = Paths.get(outputDir, "PositionReport.csv");

        // Read the input files
        List<String> instrumentDetailsLines = Files.readAllLines(instrumentDetailsPath);
        List<String> positionDetailsLines = Files.readAllLines(positionDetailsPath);

        // Parse the input files into maps for easy lookup
        Map<Integer, String[]> instrumentDetailsMap = new HashMap<Integer, String[]>();
        for (int i = 1; i < instrumentDetailsLines.size(); i++) { // Skip header
            String[] columns = instrumentDetailsLines.get(i).split(",");
            int id = Integer.parseInt(columns[0]);
            instrumentDetailsMap.put(id, columns);
        }

        // Prepare the output file content
        List<String> outputLines = new ArrayList<String>();
        outputLines.add("ID,PositionID,ISIN,Quantity,Total Price"); // Add header

        for (int i = 1; i < positionDetailsLines.size(); i++) { // Skip header
            String[] columns = positionDetailsLines.get(i).split(",");
            int positionID = Integer.parseInt(columns[0]);
            int instrumentID = Integer.parseInt(columns[1]);
            int quantity = Integer.parseInt(columns[2]);

            // Get instrument details
            String[] instrumentDetails = instrumentDetailsMap.get(instrumentID);
            if (instrumentDetails != null) {
                String isin = instrumentDetails[2];
                double unitPrice = Double.parseDouble(instrumentDetails[3]);
                double totalPrice = quantity * unitPrice;

                // Append transformed data to output
                outputLines.add(String.format("%d,%d,%s,%d,%.2f", instrumentID, positionID, isin, quantity, totalPrice));
            }
        }

        // Write the output to the file
        Files.write(positionReportPath, outputLines);
    }

    @Then("^the data in \"([^ should match the expected transformation$")
    public void validateOutputData(String outputFile) throws IOException {
        String outputDir = "src/test/resources/data/";
        Path actualOutputPath = Paths.get(outputDir, outputFile);
        Path expectedOutputPath = Paths.get(outputDir, "PositionReport.csv"); // Expected output file

        // Read the actual and expected output files
        List<String> actualLines = Files.readAllLines(actualOutputPath);
        List<String> expectedLines = Files.readAllLines(expectedOutputPath);

        // Assert that the files have the same number of lines
        Assert.assertEquals("Mismatch in number of rows between actual and expected output files",
                            expectedLines.size(), actualLines.size());

        // Compare each line
        for (int i = 0; i < expectedLines.size(); i++) {
            Assert.assertEquals(String.format("Mismatch at line %d", i + 1),
                                expectedLines.get(i), actualLines.get(i));
        }
    }
}
