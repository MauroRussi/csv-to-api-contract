package co.com.mrsoft.utils.api;

import co.com.mrsoft.utils.api.csv.CSVParser;
import co.com.mrsoft.utils.api.csv.model.Record;
import co.com.mrsoft.utils.api.raml.RAMLGenerator;

import java.util.Collection;

public class CSVToAPIContract {
    public static void main(String[] args) {
        String dummyInputFile = "./input/dummy.txt";
        String dummyOutputFile = "./input/dummyOutput.raml";

        try {
            System.out.println("Reading and parsing CSV input file...");
            CSVParser parser = new CSVParser(dummyInputFile);
            Collection<Record> records = parser.parse();
            System.out.println("Records read from CSV: " + records.size());

            System.out.println("Generating RAML file...");
            RAMLGenerator ramlGenerator = new RAMLGenerator(dummyOutputFile);
            ramlGenerator.generate("Display name in the RAML", "Description of the RAML document", records);
            System.out.println("RAML file generated successfully!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}