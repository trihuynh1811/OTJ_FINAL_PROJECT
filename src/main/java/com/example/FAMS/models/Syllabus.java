package com.example.FAMS.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "Syllabus")
public class Syllabus {

    @Id
    @Column(name = "topic_code", nullable = false)
    private String topicCode;

    @Column(name = "topic_name", nullable = false)
    private String topicName;

    @Column(name = "technical_group", nullable = false)
    private String technicalGroup;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "training_audience", nullable = false)
    private String trainingAudience;

    @Column(name = "topic_outline", nullable = false)
    private String topicOutline;

    @Column(name = "training_materials", nullable = false)
    private String trainingMaterials;

    @Column(name = "training_principles", nullable = false)
    private String trainingPrinciples;

    @Column(name = "priority", nullable = false)
    private String priority;

    @Column(name = "publish_status", nullable = false)
    private String publishStatus;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;

    @Column(name = "modified_date", nullable = false)
    private Date modifiedDate;

    @ManyToOne
    @JoinColumn(name = "Users_id")
    private User userID;

    @OneToMany(mappedBy = "topicCode")
    private final Set<TrainingProgramSyllabus> tps = new HashSet<>();

    @OneToMany(mappedBy = "topicCode")
    private final Set<TrainingUnit> tu = new HashSet<>();

}
