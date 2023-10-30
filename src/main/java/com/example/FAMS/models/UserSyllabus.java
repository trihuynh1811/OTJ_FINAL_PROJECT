package com.example.FAMS.models;

import com.example.FAMS.models.composite_key.UserSyllabusCompositeKey;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_syllabus")
public class UserSyllabus {

    @EmbeddedId
    UserSyllabusCompositeKey id;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private User userId;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @MapsId("topicCode")
    @JoinColumn(name = "topic_code", referencedColumnName = "topic_code")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private Syllabus topicCode;



    @Column(name = "user_type")
    private String userType;

}
