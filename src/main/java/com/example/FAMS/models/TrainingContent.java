package com.example.FAMS.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TrainingContents")
public class TrainingContent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "content_code")
    private int contentCode;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumns({
            @JoinColumn(name="unit_code", referencedColumnName="unit_code"),
            @JoinColumn(name="topic_code", referencedColumnName="topic_code")
    })
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ToString.Exclude
    @JsonBackReference
    private TrainingUnit unitCode;

    @OneToMany(mappedBy = "contentCode", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private final Set<LearningObjective> learningObjectives = new HashSet<>();

    @Column(nullable = false, name = "delivery_type")
    private String deliveryType;

    @Column(nullable = false, name = "duration")
    private int duration;

    @Column(nullable = false, name = "training_format")
    private boolean trainingFormat;

    @Column(name = "content_name")
    private String content_name;

    @Column(name = "note")
    private String note;

    public void addLearningObjective(LearningObjective learningObjective){
        learningObjectives.add(learningObjective);
        learningObjective.setContentCode(this);
    }


}
