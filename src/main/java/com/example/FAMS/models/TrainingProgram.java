package com.example.FAMS.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;

import java.util.Date;

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
    private String code;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "user_id")
    private long userID;

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

    @Column(nullable = false, name = "midified_date")
    private Date modifiedDate;
}
