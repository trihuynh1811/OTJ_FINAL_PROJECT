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
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MonthOfYear {

    @Id
    private int monthId;

    private String monthName;

    @OneToMany(mappedBy = "monthId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<TrainingCalendar> trainingCalendars = new HashSet<>();
}
