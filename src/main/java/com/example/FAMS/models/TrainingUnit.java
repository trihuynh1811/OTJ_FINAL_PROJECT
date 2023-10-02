package com.example.FAMS.models;

import com.example.FAMS.models.composite_key.SyllabusTrainingContentCompositeKey;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TrainingUnit")
public class TrainingUnit {

    @EmbeddedId
    SyllabusTrainingContentCompositeKey id;

    @ManyToOne
    @MapsId("unitCode")
    @JoinColumn(name = "unit_code")
    private TrainingContent unitCode;

    @ManyToOne
    @MapsId("topicCode")
    @JoinColumn(name = "topic_code")
    private Syllabus topicCode;

    @Column(name = "unit_name", nullable = false)
    private String unitName;

    @Column(name = "day_number", nullable = false)
    private int dayNumber;
}
