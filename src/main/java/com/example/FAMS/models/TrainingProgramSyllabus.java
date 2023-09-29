package com.example.FAMS.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TrainingProgramSyllabuses")
public class TrainingProgramSyllabus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "syllabus_topic_code")
    private Syllabus topicCode;


    @ManyToOne
    @JoinColumn(name = "training_program_code")
    private TrainingProgram trainingProgramCode;

    @Column(name = "sequence", nullable = false)
    private String sequence;
}
