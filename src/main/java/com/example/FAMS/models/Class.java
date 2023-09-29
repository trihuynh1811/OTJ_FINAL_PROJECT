package com.example.FAMS.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(name = "class_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int classId;

    @ManyToOne
    @JoinColumn(name = "training_program_code")
    private TrainingProgram trainingProgram;

    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "class_code", nullable = false)
    private String classCode;

    @Column(name = "duration", nullable = false)
    private String duration;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "fsu", nullable = false)
    private String fsu;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;

    @Column(name = "modified_date", nullable = false)
    private Date modifiedDate;

    @OneToMany(mappedBy = "classID")
    @JsonManagedReference
    private Set<ClassUser> classUsers = new HashSet<>();

}
