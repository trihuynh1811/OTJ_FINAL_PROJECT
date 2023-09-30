package com.example.FAMS.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TrainingPrograms")
public class TrainingProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "training_program_code")
    private String trainingProgramCode;

    @Column(nullable = false, name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User userID;

    @Column(nullable = false, name = "start_date")
    private Date startDate;

    @Column(nullable = false, name = "duration")
    private int duration;

    @Column(nullable = false, name = "topic_code")
    private String topicCode;

    @Column(nullable = false, name = "status")
    private String status;

    @Column(nullable = false, name = "created_by")
    private String createdBy;

    @Column(nullable = false, name = "created_date")
    private Date createdDate;

    @Column(nullable = false, name = "modified_by")
    private String modifiedBy;

    @Column(nullable = false, name = "modified_date")
    private Date modifiedDate;

    @OneToMany(mappedBy = "trainingProgramCode")
    @JsonManagedReference
    private final Set<TrainingProgramSyllabus> trainingProgramSyllabus = new HashSet<>();

    @OneToMany(mappedBy = "trainingProgram")
    @JsonManagedReference
    private final Set<Class> classes = new HashSet<>();


}
