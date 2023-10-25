package com.example.FAMS.dto.responses;

import java.sql.Time;
import java.util.Date;

public interface CalendarDayResponse {
    public Time getTimeFrom();

    public Time getTimeTo();

    public Integer getClassId();

    public String getClassCode();

    public String getStatus();

    public String getUserType();

    public Integer getNumberOfDay();

    public String getName();

    public Date getStartDate();

    public Date getEndDate();
}
