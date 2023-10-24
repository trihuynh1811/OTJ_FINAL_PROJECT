package com.example.FAMS.models;


import com.example.FAMS.models.composite_key.ClassUserCompositeKey;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "ClassUser")
public class ClassUser {

    @EmbeddedId
    ClassUserCompositeKey id;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @MapsId("classId")
    @JoinColumn(name = "class_id")
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Class classID;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "users_id")
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User userID; //ADMIN or MENTOR or STUDENT

    @Column(nullable = false)
    private String userType;


}
