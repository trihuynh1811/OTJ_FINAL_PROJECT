package com.example.FAMS.models;


import com.example.FAMS.models.composite_key.ClassUserCompositeKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "ClassUser")
public class ClassUser {

    @EmbeddedId
    ClassUserCompositeKey id;

    @ManyToOne
    @MapsId("classId")
    @JoinColumn(name = "class_id")
    private Class classID;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "users_id")
    private User userID;

    @Column(nullable = false)
    private String userType;
}
