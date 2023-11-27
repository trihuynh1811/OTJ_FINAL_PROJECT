package com.example.FAMS.dto.responses;

import java.sql.Time;
import java.util.Date;

public interface CalendarWeekResponse {
    public Time getTimeFrom();

    public Time getTimeTo();

    public String getClassCode();

    public String getStatus();

    public Date getEnrollDate();

    public Date getStartDate();

    public Date getEndDate();
}
