package org.travelsystem;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.travelsystem.common.TouchType;
import org.travelsystem.dto.Touch;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.travelsystem.common.TouchType.OFF;
import static org.travelsystem.common.TouchType.ON;


public class TravelSystem {
    public static final String INCOMPLETE = "INCOMPLETE";
    public static final String COMPLETED = "COMPLETED";
    public static final String CANCELLED = "CANCELLED";
    public static final String[] TRIPS_HEADER = {"started", "finished", "DurationSec", "fromStopId", "toStopId", "ChargeAmount", "CompanyId", "BusId", "HashedPan", "Status"};
    public static final String[] UNPROCESSABLE_HEADER = {
            "started",
            "finished",
            "DurationSec",
            "fromStopId",
            "toStopId",
            "ChargeAmount",
            "CompanyId",
            "BusId",
            "HashedPan",
            "Status"
    };
    public static final String[] SUMMARY_HEADER = {
            "date",
            "CompanyId",
            "BusId",
            "CompleteTripCount",
            "IncompleteTripCount",
            "CancelledTripCount",
            "TotalCharges",

    };
    private final String tripsFilePath;
    private final String unprocessableTouchDataFilePath;
    private final String summaryFilePath;

    private static final double AB_COST = 4.5;
    private static final double BC_COST = 6.25;
    private static final double AC_COST = 8.45;
    private static final double NO_CHARGE = 0;
    private static final String STOP_A = "StopA";
    private static final String STOP_B = "StopB";
    private static final String STOP_C = "StopC";
    private final List<String[]> trips;
    private final List<String[]> unprocessableTouches;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");


    public TravelSystem(String tripsFilePath, String unprocessableTouchDataFilePath, String summaryFilePath) {
        this.tripsFilePath = tripsFilePath;
        this.unprocessableTouchDataFilePath = unprocessableTouchDataFilePath;
        this.summaryFilePath = summaryFilePath;
        this.busTouches = new HashMap<>();
        this.trips = new LinkedList<>();
        this.unprocessableTouches = new LinkedList<>();
    }

    public void process(String filepath) {
        try {

            CSVReader csvReader = new CSVReader(new FileReader(filepath));

            CSVWriter tripsWriter = new CSVWriter(new FileWriter(tripsFilePath),
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
            CSVWriter unprocessableTouchDataWriter = new CSVWriter(new FileWriter(unprocessableTouchDataFilePath),
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
            CSVWriter summaryWriter = new CSVWriter(new FileWriter(summaryFilePath),
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);

            tripsWriter.writeNext(TRIPS_HEADER);
            unprocessableTouchDataWriter.writeNext(UNPROCESSABLE_HEADER);
            summaryWriter.writeNext(SUMMARY_HEADER);


            csvReader.readNext();
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                Touch touch;
                try {
                    touch = new Touch(
                            Integer.parseInt(nextRecord[0]),
                            DateTime.parse(nextRecord[1], dateTimeFormatter).withZoneRetainFields(DateTimeZone.UTC),
                            getTouchType(nextRecord[2]),
                            nextRecord[3],
                            nextRecord[4],
                            nextRecord[5],
                            DigestUtils.sha256Hex(nextRecord[6])
                    );
                } catch (Exception e) {
                    // catch here
                    System.out.println(e.getMessage());
                    continue;
                }

                buildDataStructure(touch);
            }
            processTouches();
            tripsWriter.writeAll(trips);
            unprocessableTouchDataWriter.writeAll(unprocessableTouches);


            tripsWriter.close();
            unprocessableTouchDataWriter.close();
            summaryWriter.close();
        } catch (IOException | CsvValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    private void buildDataStructure(Touch touch) {
        List<Touch> touches = busTouches.computeIfAbsent(touch.getBusId(), k -> new LinkedList<>());
        touches.add(touch);
    }

    private void processTouches() {
        for (List<Touch> touches : busTouches.values()) {
            Map<String, Touch> onTouchMap = new HashMap<>();
            for (Touch touch : touches) {
                if (touch.getTouchType() == ON) {
                    if (onTouchMap.containsKey(touch.getPan())) {
                        unprocessableTouches.add(setupUnprocessableTouch(touch, "On type touch duplication"));
                    } else {
                        onTouchMap.put(touch.getPan(), touch);
                    }
                }
                if (touch.getTouchType() == OFF) {
                    if (!onTouchMap.containsKey(touch.getPan())) {
                        unprocessableTouches.add(setupUnprocessableTouch(touch, "Off type touch without On type"));
                    } else {
                        Touch onTouch = onTouchMap.remove(touch.getPan());
                        trips.add(setupTrip(onTouch, touch));
                    }
                }
            }
            if (onTouchMap.size() > 0) {
                for (Touch onTouch : onTouchMap.values()) {
                    trips.add(setupTrip(onTouch, null));
                }
            }
        }
    }

    private String[] setupUnprocessableTouch(Touch touch, String reason) {
        return new String[]{
                dateTimeFormatter.print(touch.getDatetime()),
                getTouchTypeString(touch.getTouchType()),
                touch.getStopId(),
                touch.getCompanyId(),
                touch.getBusId(),
                touch.getPan(),
                reason
        };
    }

    private String[] setupTrip(Touch onTouch, Touch offTouch) {
        String status = getTripStatus(onTouch, offTouch);
        Touch lastTouch = COMPLETED.equals(status) ? offTouch : onTouch;
        return new String[]{
                dateTimeFormatter.print(onTouch.getDatetime()),
                dateTimeFormatter.print(lastTouch.getDatetime()),
                String.valueOf(Seconds.secondsBetween(onTouch.getDatetime(), lastTouch.getDatetime()).getSeconds()),
                onTouch.getStopId(),
                lastTouch.getStopId(),
                String.valueOf(calculatedFee(onTouch, offTouch)),
                onTouch.getCompanyId(),
                onTouch.getBusId(),
                onTouch.getPan(),
                status
        };
    }

    private String getTripStatus(Touch onTouch, Touch offTouch) {
        if (offTouch == null) {
            return INCOMPLETE;
        } else if (!onTouch.getStopId().equals(offTouch.getStopId())) {
            return COMPLETED;
        } else {
            return CANCELLED;
        }
    }

    private double calculatedFee(Touch onTouch, Touch offTouch) {
        if (offTouch == null) {
            switch (onTouch.getStopId()) {
                case STOP_A:
                    return AC_COST;
                case STOP_B:
                    return BC_COST;
                case STOP_C:
                    return AC_COST;
                default:
                    throw new RuntimeException("invalid stop");
            }
        } else if (onTouch.getStopId().equals(offTouch.getStopId())) {
            return NO_CHARGE;
        } else if (
                onTouch.getStopId().equals(STOP_A) && offTouch.getStopId().equals(STOP_B) ||
                        offTouch.getStopId().equals(STOP_A) && onTouch.getStopId().equals(STOP_B)
        ) {
            return AB_COST;
        } else if (
                onTouch.getStopId().equals(STOP_B) && offTouch.getStopId().equals(STOP_C) ||
                        offTouch.getStopId().equals(STOP_B) && onTouch.getStopId().equals(STOP_C)
        ) {
            return BC_COST;
        } else if (
                onTouch.getStopId().equals(STOP_A) && offTouch.getStopId().equals(STOP_C) ||
                        offTouch.getStopId().equals(STOP_A) && onTouch.getStopId().equals(STOP_C)
        ) {
            return AC_COST;
        } else {
            throw new RuntimeException("invalid stop");
        }
    }

    private TouchType getTouchType(String type) {
        return type.equals("ON") ? ON : OFF;
    }

    private String getTouchTypeString(TouchType type) {
        return type == ON ? "ON" : "OFF";
    }

    private Map<String, List<Touch>> busTouches;

}
