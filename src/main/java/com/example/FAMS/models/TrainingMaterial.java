package com.example.FAMS.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "syllabus", referencedColumnName = "topic_code")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private Syllabus syllabus;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name="unit_code", referencedColumnName="unit_code"),
            @JoinColumn(name="topic_code", referencedColumnName="topic_code")
    })
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private TrainingUnit trainingUnit;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "training_content", referencedColumnName = "content_code")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private TrainingContent trainingContent;
}
