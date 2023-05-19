package org.travelsystem;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.travelsystem.common.TouchType;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.format.FormatStyle;

import static org.travelsystem.common.TouchType.OFF;
import static org.travelsystem.common.TouchType.ON;

public class TravelSystem {
    private final String tripsFilePath;
    private final String unprocessableTouchDataFilePath;
    private final String summaryFilePath;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");

    public TravelSystem(String tripsFilePath, String unprocessableTouchDataFilePath, String summaryFilePath) {
        this.tripsFilePath = tripsFilePath;
        this.unprocessableTouchDataFilePath = unprocessableTouchDataFilePath;
        this.summaryFilePath = summaryFilePath;
    }

    public void process(String filepath) {
        try {

            CSVReader csvReader = new CSVReader(new FileReader(filepath));
            CSVWriter tripsWriter = new CSVWriter(new FileWriter(new File(tripsFilePath)));
            CSVWriter unprocessableTouchDataWriter = new CSVWriter(new FileWriter(new File(unprocessableTouchDataFilePath)));
            CSVWriter summaryWriter = new CSVWriter(new FileWriter(new File(summaryFilePath)));

            String[] header = {"Name", "Class", "Marks"};
            tripsWriter.writeNext(header);
            String[] header2 = {"Name", "Class", "Marks"};
            unprocessableTouchDataWriter.writeNext(header2);
            String[] header3 = {"Name", "Class", "Marks"};
            summaryWriter.writeNext(header3);

            tripsWriter.close();
            unprocessableTouchDataWriter.close();
            summaryWriter.close();

            csvReader.readNext();
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                readTouch(Integer.parseInt(nextRecord[0]), DateTime.parse(nextRecord[1], dateTimeFormatter).withZoneRetainFields(DateTimeZone.UTC), getTouchType(nextRecord[2]), nextRecord[3], nextRecord[4], nextRecord[5], nextRecord[6]);
                for (String cell : nextRecord) {
                    System.out.print(cell + "\t");
                }
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private TouchType getTouchType(String type) {
        return type.equals("ON") ? ON : OFF;
    }

    private void readTouch(int id, DateTime datetime, TouchType touchType, String stopId, String companyId, String busId, String Pan) {
        System.out.println(datetime.toString());
    }

}
