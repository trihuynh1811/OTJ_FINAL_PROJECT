package com.example.FAMS.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "Syllabus")
public class Syllabus {

    @OneToMany(mappedBy = "topicCode")
    @JsonManagedReference
    private final Set<TrainingProgramSyllabus> tps = new HashSet<>();
    @OneToMany(mappedBy = "topicCode")
    @JsonManagedReference
    private final Set<TrainingUnit> tu = new HashSet<>();
    @Id
    @Column(name = "topic_code", nullable = false)
    private String topicCode;
    @Column(name = "topic_name", nullable = false)
    private String topicName;
    @Lob
    @Column(name = "technical_group", nullable = false)
    private String technicalGroup;
    @Column(nullable = false)
    private String version;
    @Column(name = "training_audience", nullable = false)
    private int trainingAudience;
    @Column(name = "topic_outline", nullable = false)
    private String topicOutline;
    @Column(name = "training_materials", nullable = false)
    private String trainingMaterials;
    @Column(name = "training_principles", nullable = false)
    private String trainingPrinciples;
    @Column(nullable = false)
    private String priority;
    @Column(name = "publish_status", nullable = false)
    private String publishStatus;
    @Column(name = "created_by", nullable = false)
    private String createdBy;
    @Temporal(TemporalType.DATE)
    @Column(name = "created_date", nullable = false)
    private Date createdDate;
    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;
    @Temporal(TemporalType.DATE)
    @Column(name = "modified_date", nullable = false)
    private Date modifiedDate;
    @ManyToOne
    @JoinColumn(nullable = false, name = "user_syllabus")
    private User userID;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "syllabus_objective",
            joinColumns = {@JoinColumn(name = "syllabus_code")},
            inverseJoinColumns = {@JoinColumn(name = "objective_code")}
    )
    @JsonManagedReference
    private Set<LearningObjective> learningObjectives;
}
