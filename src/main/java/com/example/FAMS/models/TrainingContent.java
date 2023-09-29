package com.example.FAMS.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TrainingContents")
public class TrainingContent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false, name = "learning_object")
    private String learningObjective;

    @Column(nullable = false, name = "delivery_type")
    private String deliveryType;

    @Column(nullable = false, name = "duration")
    private int duration;

    @Column(nullable = false, name = "training_format")
    private String trainingFormat;

    @Column(nullable = false, name = "note")
    private String note;

    @Column(nullable = false, name = "unit_code")
    private String unitCode;
}
