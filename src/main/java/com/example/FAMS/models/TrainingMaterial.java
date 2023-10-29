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
}
