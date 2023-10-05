package com.example.FAMS.services;

import com.example.FAMS.models.Syllabus;

import java.util.List;

public interface SyllabusService {

    public List<Syllabus> getSyllabuses();

    List<Syllabus> getDetailSyllabus();

    public Syllabus createSyllabus(String topicName, String topicCode, String version, int numberOfAudience);
}
