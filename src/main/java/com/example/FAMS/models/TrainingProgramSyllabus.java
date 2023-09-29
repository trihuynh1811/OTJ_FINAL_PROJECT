package com.example.FAMS.models;

import com.example.FAMS.models.composite_key.SyllabusTrainingProgramCompositeKey;
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

    @ManyToOne
    @MapsId("topicCode")
    @JoinColumn(name = "topic_code")
    private Syllabus topicCode;


    @ManyToOne
    @MapsId("trainingProgramCode")
    @JoinColumn(name = "training_programs_code")
    private TrainingProgram trainingProgramCode;

    @Column(name = "sequence", nullable = false)
    private String sequence;
}
