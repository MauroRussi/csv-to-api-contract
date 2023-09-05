package co.com.mrsoft.utils.api;

import co.com.mrsoft.utils.api.csv.CSVParser;
import co.com.mrsoft.utils.api.csv.model.Record;

import java.util.Collection;

public class CSVToAPIContract {
    public static void main(String[] args) {
        String dummyFile = "./input/dummy.txt";

        try {
            CSVParser parser = new CSVParser(dummyFile);
            Collection<Record> records = parser.parse();
            System.out.println("Records: ");
            System.out.println(records);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}