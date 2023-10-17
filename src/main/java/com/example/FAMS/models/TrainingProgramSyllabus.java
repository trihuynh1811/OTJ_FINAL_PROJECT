package com.example.FAMS.models;

import com.example.FAMS.models.composite_key.SyllabusTrainingProgramCompositeKey;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TrainingProgramSyllabuses")
public class TrainingProgramSyllabus {

    @EmbeddedId
    SyllabusTrainingProgramCompositeKey id;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.EAGER)
    @MapsId("topicCode")
    @JoinColumn(name = "topic_code", referencedColumnName = "topic_code")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private Syllabus topicCode;


    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.EAGER)
    @MapsId("trainingProgramCode")
    @JoinColumn(name = "training_program_code", referencedColumnName = "training_program_code")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private TrainingProgram trainingProgramCode;

    @Column(name = "sequence", nullable = false)
    private String sequence;
}
