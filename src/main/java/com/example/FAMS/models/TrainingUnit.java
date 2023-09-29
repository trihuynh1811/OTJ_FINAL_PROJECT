package com.example.FAMS.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TrainingUnit")
public class TrainingUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "TrainingContents_unitCode")
    private TrainingContent unitCode;

    @Column(name = "unit_name", nullable = false)
    private String unitName;

    @Column(name = "day_number", nullable = false)
    private int dayNumber;

    @ManyToOne
    @JoinColumn(name = "syllabus_topic_code")
    private Syllabus topicCode;
}
