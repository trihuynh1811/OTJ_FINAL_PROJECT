package com.example.FAMS.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class LearningObjective {

    @Id
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String description;

    @ManyToMany
    @JoinTable(
            name = "SyllabusObjective",
            joinColumns = {@JoinColumn(name = "objective_code")},
            inverseJoinColumns = {@JoinColumn(name = "topic_code")}
    )
    private final Set<Syllabus> s = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "learning_objective")
    private TrainingContent learningObjective;
}
