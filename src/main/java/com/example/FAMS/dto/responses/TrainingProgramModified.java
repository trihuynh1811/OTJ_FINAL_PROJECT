package com.example.FAMS.dto.responses;

import java.util.Date;

public interface TrainingProgramModified {
    public int getTrainingProgramCode();

    public String getName();

    public int getDuration();

    public String getStatus();

    public String getCreatedBy();

    public Date getCreatedDate();
}
