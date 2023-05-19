package org.travelsystem.dto;

import org.joda.time.DateTime;
import org.travelsystem.common.TouchType;

public class Touch {
    private int id;
    private DateTime datetime;
    private TouchType touchType;
    private String stopId;
    private String companyId;
    private String busId;
    private String pan;

    public Touch(int id, DateTime datetime, TouchType touchType, String stopId, String companyId, String busId, String pan) {
        this.id = id;
        this.datetime = datetime;
        this.touchType = touchType;
        this.stopId = stopId;
        this.companyId = companyId;
        this.busId = busId;
        this.pan = pan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(DateTime datetime) {
        this.datetime = datetime;
    }

    public TouchType getTouchType() {
        return touchType;
    }

    public void setTouchType(TouchType touchType) {
        this.touchType = touchType;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
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

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }
}
