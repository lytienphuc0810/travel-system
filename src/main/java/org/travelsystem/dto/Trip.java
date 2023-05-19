package org.travelsystem.dto;

public class Trip {
    private String started;
    private String finished;
    private int durationSec;
    private String fromStopId;
    private String toStopId;
    private double chargeAmount;
    private String companyId;
    private String busId;
    private String hashedPan;
    private String status;

    public Trip(String started, String finished, int durationSec, String fromStopId, String toStopId, double chargeAmount, String companyId, String busId, String hashedPan, String status) {
        this.started = started;
        this.finished = finished;
        this.durationSec = durationSec;
        this.fromStopId = fromStopId;
        this.toStopId = toStopId;
        this.chargeAmount = chargeAmount;
        this.companyId = companyId;
        this.busId = busId;
        this.hashedPan = hashedPan;
        this.status = status;
    }
}
