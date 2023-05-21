package org.travelsystem;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;


public class TestTravelSystem {
    public static final String TESTOUTPUT_UNPROCESSABLE_TOUCH_DATA_CSV = "testoutput/unprocessableTouchData.csv";
    public static final String TESTOUTPUT_SUMMARY_CSV = "testoutput/summary.csv";
    public static final String TESTOUTPUT_TRIPS_CSV = "testoutput/trips.csv";
    private static final List<String[]> expectedSummary = Arrays.asList(
            new String[]{"date", "CompanyId", "BusId", "CompleteTripCount", "IncompleteTripCount", "CancelledTripCount", "TotalCharges"},
            new String[]{"16-05-2023", "Company1", "Bus10", "2", "1", "0", "17.0"},
            new String[]{"16-05-2023", "Company1", "Bus9", "2", "0", "1", "10.75"}
    );
    private static final List<String[]> expectedTrips = Arrays.asList(
            new String[]{"started", "finished", "DurationSec", "fromStopId", "toStopId", "ChargeAmount", "CompanyId", "BusId", "HashedPan", "Status"},
            new String[]{"16-05-2023 12:15:00", "16-05-2023 12:25:00", "600", "StopA", "StopB", "4.5", "Company1", "Bus9", "ae95c9d5f01369d7cfe1b2ba31a5d9e09db76d4987e5ab96cf4b22fe5a1fae37", "COMPLETED"},
            new String[]{"16-05-2023 12:30:00", "16-05-2023 12:45:00", "900", "StopB", "StopC", "6.25", "Company1", "Bus9", "ae95c9d5f01369d7cfe1b2ba31a5d9e09db76d4987e5ab96cf4b22fe5a1fae37", "COMPLETED"},
            new String[]{"16-05-2023 12:50:00", "16-05-2023 12:50:00", "0", "StopB", "StopB", "0.0", "Company1", "Bus9", "ae95c9d5f01369d7cfe1b2ba31a5d9e09db76d4987e5ab96cf4b22fe5a1fae37", "CANCELLED"},
            new String[]{"16-05-2023 12:15:00", "16-05-2023 12:25:00", "600", "StopA", "StopB", "4.5", "Company1", "Bus10", "805093f416a0187eed4e910bc464e869881929eb11160783dff05c5839d2ba27", "COMPLETED"},
            new String[]{"16-05-2023 12:30:00", "16-05-2023 12:45:00", "900", "StopB", "StopC", "6.25", "Company1", "Bus10", "805093f416a0187eed4e910bc464e869881929eb11160783dff05c5839d2ba27", "COMPLETED"},
            new String[]{"16-05-2023 12:50:00", "16-05-2023 12:50:00", "0", "StopB", "StopB", "6.25", "Company1", "Bus10", "805093f416a0187eed4e910bc464e869881929eb11160783dff05c5839d2ba27", "INCOMPLETE"}
    );
    private static final List<String[]> expectedUnprocessableTouchData = Arrays.asList(
            new String[]{"ID", "DateTimeUTC", "TouchType", "StopID", "CompanyID", "BusID", "HashedPan"},
            new String[]{"asdasdasd", "16-05-2023 12:55:00", "OFF", "StopB", "Company1", "Bus9", "2255550000666661", "NumberFormatException For input string: \"asdasdasd\""},
            new String[]{"16-05-2023 12:50:00", "ON", "StopB", "Company1", "Bus9", "ae95c9d5f01369d7cfe1b2ba31a5d9e09db76d4987e5ab96cf4b22fe5a1fae37", "On touch duplication"},
            new String[]{"16-05-2023 12:50:00", "ON", "StopB", "Company1", "Bus9", "ae95c9d5f01369d7cfe1b2ba31a5d9e09db76d4987e5ab96cf4b22fe5a1fae37", "On touch duplication"},
            new String[]{"16-05-2023 12:50:00", "ON", "StopB", "Company1", "Bus9", "ae95c9d5f01369d7cfe1b2ba31a5d9e09db76d4987e5ab96cf4b22fe5a1fae37", "On touch duplication"},
            new String[]{"16-05-2023 12:50:00", "ON", "StopB", "Company1", "Bus9", "ae95c9d5f01369d7cfe1b2ba31a5d9e09db76d4987e5ab96cf4b22fe5a1fae37", "On touch duplication"},
            new String[]{"16-05-2023 12:50:00", "ON", "StopB", "Company1", "Bus9", "ae95c9d5f01369d7cfe1b2ba31a5d9e09db76d4987e5ab96cf4b22fe5a1fae37", "On touch duplication"},
            new String[]{"16-05-2023 12:50:00", "ON", "StopB", "Company1", "Bus10", "805093f416a0187eed4e910bc464e869881929eb11160783dff05c5839d2ba27", "On touch duplication"},
            new String[]{"16-05-2023 12:50:00", "ON", "StopB", "Company1", "Bus10", "805093f416a0187eed4e910bc464e869881929eb11160783dff05c5839d2ba27", "On touch duplication"},
            new String[]{"16-05-2023 12:50:00", "ON", "StopB", "Company1", "Bus10", "805093f416a0187eed4e910bc464e869881929eb11160783dff05c5839d2ba27", "On touch duplication"},
            new String[]{"16-05-2023 12:50:00", "ON", "StopB", "Company1", "Bus10", "805093f416a0187eed4e910bc464e869881929eb11160783dff05c5839d2ba27", "On touch duplication"}

    );

    @Test
    public void testTravelSystem() {
        assertEquals(7, 7);

        TravelSystem travelSystem = new TravelSystem(
                "testoutput/trips.csv", "testoutput/unprocessableTouchData.csv", "testoutput/summary.csv"
        );
        travelSystem.process("testinput/touchData.csv");


        try {
            this.compareCSV(new CSVReader(new FileReader(TESTOUTPUT_TRIPS_CSV)), expectedTrips);
            this.compareCSV(new CSVReader(new FileReader(TESTOUTPUT_SUMMARY_CSV)), expectedSummary);
            this.compareCSV(new CSVReader(new FileReader(TESTOUTPUT_UNPROCESSABLE_TOUCH_DATA_CSV)), expectedUnprocessableTouchData);

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

    }

    private void compareCSV(CSVReader csvReader, List<String[]> expected) throws CsvValidationException, IOException {
        String[] nextRecord;

        while ((nextRecord = csvReader.readNext()) != null) {
            assertLinesMatch(Arrays.asList(nextRecord), Arrays.asList(expected.get((int) csvReader.getLinesRead() - 1)));
        }
    }
}
