package com.example.FAMS.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TrainingContents")
public class TrainingContent {

    @Id
    @Column(nullable = false, name = "unit_code")
    private String unitCode;

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

    @OneToMany(mappedBy = "unitCode")
    @JsonManagedReference
    private final Set<TrainingUnit> tu = new HashSet<>();

    @OneToMany(mappedBy = "trainingContent")
    @JsonManagedReference
    private final Set<LearningObjective> learningObjectives = new HashSet<>();
}
