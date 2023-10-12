package com.example.FAMS.models;

import com.example.FAMS.models.composite_key.SyllabusTrainingProgramCompositeKey;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @EmbeddedId
    SyllabusTrainingProgramCompositeKey id;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @MapsId("topicCode")
    @JoinColumn(name = "topic_code", referencedColumnName = "topic_code")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private Syllabus topicCode;


    @ManyToOne
    @MapsId("trainingProgramCode")
    @JoinColumn(name = "training_programs_code")
    private TrainingProgram trainingProgramCode;

    @Column(name = "sequence", nullable = false)
    private String sequence;
}
