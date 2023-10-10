package com.example.FAMS.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class LearningObjective {

    @Id
    @Column(name = "code")
    private String learningObjectiveCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "syllabus_objective",
            joinColumns = {@JoinColumn(name = "objective_code")},
            inverseJoinColumns = {@JoinColumn(name = "syllabus_code")}
    )
    @JsonManagedReference
    private Set<Syllabus> syllabus;

    @ManyToOne
    @JoinColumn(name = "training_content")
    private TrainingContent trainingContent;
}
