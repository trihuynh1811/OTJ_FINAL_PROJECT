package com.example.FAMS.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "training_material")
public class TrainingMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(nullable = false)
    private String material;

    @Column(nullable = false)
    private String source;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "syllabus")
    private Syllabus syllabus;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name="unit_code", referencedColumnName="unit_code"),
            @JoinColumn(name="topic_code", referencedColumnName="topic_code")
    })
    private TrainingUnit trainingUnit;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "training_content")
    private TrainingContent trainingContent;
}
