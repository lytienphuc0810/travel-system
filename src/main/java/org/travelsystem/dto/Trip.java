package org.travelsystem.dto;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormatter;


public class Trip {
    private DateTime started;
    private DateTime finished;
    private int durationSec;
    private String fromStopId;
    private String toStopId;
    private double chargeAmount;
    private String companyId;
    private String busId;
    private String hashedPan;
    private String status;

    public Trip(DateTime started, DateTime finished, int durationSec, String fromStopId, String toStopId, double chargeAmount, String companyId, String busId, String hashedPan, String status) {
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

    public String[] toStringArray(DateTimeFormatter dateTimeFormatter) {
        return new String[]{
                dateTimeFormatter.print(started),
                dateTimeFormatter.print(finished),
                String.valueOf(durationSec),
                fromStopId,
                toStopId,
                String.valueOf(chargeAmount),
                companyId,
                busId,
                hashedPan,
                status
        };
    }

    public DateTime getStarted() {
        return started;
    }

    public void setStarted(DateTime started) {
        this.started = started;
    }

    public DateTime getFinished() {
        return finished;
    }

    public void setFinished(DateTime finished) {
        this.finished = finished;
    }

    public int getDurationSec() {
        return durationSec;
    }

    public void setDurationSec(int durationSec) {
        this.durationSec = durationSec;
    }

    public String getFromStopId() {
        return fromStopId;
    }

    public void setFromStopId(String fromStopId) {
        this.fromStopId = fromStopId;
    }

    public String getToStopId() {
        return toStopId;
    }

    public void setToStopId(String toStopId) {
        this.toStopId = toStopId;
    }

    public double getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(double chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getHashedPan() {
        return hashedPan;
    }

    public void setHashedPan(String hashedPan) {
        this.hashedPan = hashedPan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
