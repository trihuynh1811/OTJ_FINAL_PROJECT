package com.example.FAMS.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "Class")
public class Class {
    @Id
    @Column(name = "class_code", nullable = false)
    private String classId;

    @ManyToOne(cascade = CascadeType.DETACH, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "training_program_code")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private TrainingProgram trainingProgram;

    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "time_from")
    private Time timeFrom;

    @Column(name = "time_to")
    private Time timeTo;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "location", nullable = false)
    private String location;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "fsu_id", referencedColumnName = "fsu_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private Fsu fsu;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "modified_by")
    private String modifiedBy = "";

    @Column(name = "modified_date")
    private Date modifiedDate = null;

    private boolean deactivated = false;

    @Column(name = "attendee")
    private String attendee;

    @Column(name = "attendee_planned")
    private int attendeePlanned = 0;

    @Column(name = "attendee_accepted")
    private int attendeeAccepted = 0;

    @Column(name = "attendee_actual")
    private int attendeeActual = 0;

    @OneToMany(mappedBy = "classID", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<ClassUser> classUsers = new HashSet<>();

    @OneToMany(mappedBy = "classId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ClassLearningDay> classLearningDays = new HashSet<>();

    @OneToMany(mappedBy = "classId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<TrainingCalendar> trainingCalendars = new HashSet<>();

}
