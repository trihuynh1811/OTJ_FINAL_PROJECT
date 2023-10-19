package com.example.FAMS.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "Class")
public class Class {
    @Id
    @Column(name = "class_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int classId;

    @ManyToOne
    @JoinColumn(name = "training_program_code")
    private TrainingProgram trainingProgram;

    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "class_code", nullable = false)
    private String classCode;

    @Column(name = "duration", nullable = false)
    private String duration;

    @Column(name = "time_from", nullable = false)
    private Time timeFrom;

    @Column(name = "time_to", nullable = false)
    private Time timeTo;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "fsu", nullable = false)
    private String fsu;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;

    @Column(name = "modified_date", nullable = false)
    private Date modifiedDate;

    @OneToMany(mappedBy = "classID")
    @JsonManagedReference
    private Set<ClassUser> classUsers = new HashSet<>();

    @OneToMany(mappedBy = "classes")
    private Set<ClassLearningDay> classLearningDays = new HashSet<>();

    public static ClassBuilder builder() {
        return new ClassBuilder();
    }

    public static class ClassBuilder {
        private int classId;
        private TrainingProgram trainingProgram;
        private String className;
        private String classCode;
        private String duration;
        private String status;
        private String location;
        private String fsu;
        private Date startDate;
        private Date endDate;
        private String createdBy;
        private Date createdDate;
        private String modifiedBy;
        private Date modifiedDate;
        private Set<ClassUser> classUsers = new HashSet<>();

        // Thêm Set để biểu diễn ngày học
        private Set<ClassLearningDay> classLearningDays = new HashSet<>();

        ClassBuilder() {
        }

        public ClassBuilder classId(int classId) {
            this.classId = classId;
            return this;
        }

        public ClassBuilder trainingProgram(TrainingProgram trainingProgram) {
            this.trainingProgram = trainingProgram;
            return this;
        }

        public ClassBuilder className(String className) {
            this.className = className;
            return this;
        }

        public ClassBuilder classCode(String classCode) {
            this.classCode = classCode;
            return this;
        }

        public ClassBuilder duration(String duration) {
            this.duration = duration;
            return this;
        }

        public ClassBuilder status(String status) {
            this.status = status;
            return this;
        }

        public ClassBuilder location(String location) {
            this.location = location;
            return this;
        }

        public ClassBuilder fsu(String fsu) {
            this.fsu = fsu;
            return this;
        }

        public ClassBuilder startDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public ClassBuilder endDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public ClassBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public ClassBuilder createdDate(Date createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public ClassBuilder modifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
            return this;
        }

        public ClassBuilder modifiedDate(Date modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this;
        }

        public ClassBuilder classUsers(Set<ClassUser> classUsers) {
            this.classUsers = classUsers;
            return this;
        }

        // Thêm phương thức để thiết lập danh sách ngày học
        public ClassBuilder classLearningDays(Set<ClassLearningDay> classLearningDays) {
            this.classLearningDays = classLearningDays;
            return this;
        }
    }
}
