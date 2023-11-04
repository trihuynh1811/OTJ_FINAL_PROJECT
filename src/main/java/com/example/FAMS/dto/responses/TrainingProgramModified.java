package com.example.FAMS.dto.responses;

import com.example.FAMS.models.TrainingProgramSyllabus;
import com.example.FAMS.models.User;

import java.util.Date;
import java.util.Set;

public interface TrainingProgramModified {
    public int getTrainingProgramCode();

    public String getName();

    public int getDuration();

    public String getStatus();

    public String getCreatedBy();

    public Date getCreatedDate();

    public Set<TrainingProgramSyllabus> getTrainingProgramSyllabus();

    public User getUserID();
}
