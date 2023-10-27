package com.example.FAMS.models;

import jakarta.persistence.*;

import java.sql.Time;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "classLearningDay")
public class ClassLearningDay {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "enroll_date", nullable = false)
    private Date enrollDate;

    @Column(name = "time_from", nullable = false)
    private Time timeFrom;

    @Column(name = "time_to", nullable = false)
    private Time timeTo;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class classId;


}
