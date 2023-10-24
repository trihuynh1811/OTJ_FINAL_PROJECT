package com.example.FAMS.models.time;

import com.example.FAMS.models.TrainingCalendar;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DateOfMonth {

    @Id
    private int dateId;

    private String dateName;

    @OneToMany(mappedBy = "dateId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<TrainingCalendar> trainingCalendars = new HashSet<>();
}
