package com.example.FAMS.models;

import com.example.FAMS.enums.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_permission")
public class UserPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int permissionId;

    @Column(name = "role",nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "syllabus",nullable = false)
    private String syllabus;

    @Column(name = "user_class",nullable = false)
    private String userClass;

    @Column(name = "learning_material",nullable = false)
    private String learningMaterial;

    @OneToMany(mappedBy = "userPermission", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<User> users = new HashSet<>();
}
