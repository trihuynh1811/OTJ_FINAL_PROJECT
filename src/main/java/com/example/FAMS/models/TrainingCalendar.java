package com.example.FAMS.models;

import com.example.FAMS.models.composite_key.DayDateMonthYearClassCompositeKey;
import com.example.FAMS.models.time.DateOfMonth;
import com.example.FAMS.models.time.DayOfWeek;
import com.example.FAMS.models.time.MonthOfYear;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class TrainingCalendar {

    @EmbeddedId
    DayDateMonthYearClassCompositeKey id;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @MapsId("dayId")
    @JoinColumn(name = "day_id")
    @JsonBackReference
    private DayOfWeek dayId;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @MapsId("dateId")
    @JoinColumn(name = "date_id")
    @JsonBackReference
    private DateOfMonth dateId;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @MapsId("monthId")
    @JoinColumn(name = "month_id")
    @JsonBackReference
    private MonthOfYear monthId;

    private int year;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @MapsId("classId")
    @JoinColumn(name = "class_id")
    @JsonBackReference
    private Class classId;

    @Column(name = "class_from")
    private Time classFrom;

    @Column(name = "class_to")
    private Time classTo;

    @Column(name = "class_type")
    private String classType;
}
