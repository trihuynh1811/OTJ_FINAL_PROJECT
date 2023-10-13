package com.example.FAMS.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

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

    @Id
    @Column(name = "topic_code", nullable = false)
    private String topicCode;

    @Column(name = "topic_name", nullable = false)
    private String topicName;

    @Column(name = "number_of_day")
    private int numberOfDay = 1;

    @Column(name = "technical_group", nullable = false, length = 2048)
    private String technicalGroup;

    @Column(nullable = false)
    private String version;

    @Column(name = "training_audience", nullable = false)
    private int trainingAudience;

    @Column(name = "topic_outline")
    private String topicOutline;

    @Column(name = "training_materials")
    private String trainingMaterials;

    @Lob
    @Column(name = "training_principles")
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

    @Column(name = "modified_by")
    private String modifiedBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "course_objective", length = 5000, nullable = false)
    private String courseObjective;

    @OneToMany(mappedBy = "topicCode")
    @JsonManagedReference
    private final Set<TrainingProgramSyllabus> tps = new HashSet<>();

    @OneToMany(mappedBy = "syllabus", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private final Set<TrainingUnit> tu = new HashSet<>();

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(nullable = false, name = "user_syllabus", referencedColumnName = "user_id")
    @JsonIgnore
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User userID;

//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinTable(name = "syllabus_objective",
//            joinColumns = {@JoinColumn(name = "syllabus_code")},
//            inverseJoinColumns = {
//                    @JoinColumn(name = "objective_code", referencedColumnName = "output_code")
//            }
//    )
//    @JsonManagedReference
//    @JsonIgnore
//    private Set<StandardOutput> standardOutputs;

//    @OneToMany(mappedBy = "topicCode", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private final Set<LearningObjective> learningObjectives = new HashSet<>();

    @OneToMany(mappedBy = "topicCode", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private final Set<SyllabusObjective> syllabusObjectives = new HashSet<>();
}
