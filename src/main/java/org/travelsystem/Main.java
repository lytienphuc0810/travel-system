package org.travelsystem;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        TravelSystem travelSystem = new TravelSystem(
                "output/trips.csv", "output/unprocessableTouchData.csv","output/summary.csv"
        );
        travelSystem.process("input/touchData.csv");
    }
}