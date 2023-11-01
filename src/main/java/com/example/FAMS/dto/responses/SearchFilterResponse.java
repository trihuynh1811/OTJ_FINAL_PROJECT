package com.example.FAMS.dto.responses;

import java.sql.Time;
import java.util.Date;

public interface SearchFilterResponse {
    public String getLocation();

    public Time getTimeFrom();

    public Time getTimeTo();
    public String getName();
    public String getStatus();
    public String getFsu();

}
