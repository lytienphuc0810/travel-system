package org.travelsystem.dto;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

public class Summary {
    private DateTime date;
    private String companyId;
    private String busId;
    private int completeTripCount;
    private int incompleteTripCount;
    private int cancelledTripCount;
    private double totalCharges;

    public Summary(DateTime date, String companyId, String busId) {
        this.date = date;
        this.companyId = companyId;
        this.busId = busId;
    }

    public String[] toStringArray(DateTimeFormatter dtf) {
        return new String[]{
                dtf.print(this.date),
                this.companyId,
                this.busId,
                String.valueOf(this.completeTripCount),
                String.valueOf(this.incompleteTripCount),
                String.valueOf(this.cancelledTripCount),
                String.valueOf(this.totalCharges)
        };
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
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

    public int getCompleteTripCount() {
        return completeTripCount;
    }

    public void setCompleteTripCount(int completeTripCount) {
        this.completeTripCount = completeTripCount;
    }

    public int getIncompleteTripCount() {
        return incompleteTripCount;
    }

    public void setIncompleteTripCount(int incompleteTripCount) {
        this.incompleteTripCount = incompleteTripCount;
    }

    public int getCancelledTripCount() {
        return cancelledTripCount;
    }

    public void setCancelledTripCount(int cancelledTripCount) {
        this.cancelledTripCount = cancelledTripCount;
    }

    public double getTotalCharges() {
        return totalCharges;
    }

    public void setTotalCharges(double totalCharges) {
        this.totalCharges = totalCharges;
    }
}
