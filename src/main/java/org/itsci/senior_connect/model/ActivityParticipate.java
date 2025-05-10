package org.itsci.senior_connect.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;

@Entity
@Table(name = "activityParticipates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityParticipate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer acparId;

    @Temporal(TemporalType.DATE)
    private Calendar acparDate;

    private String acparStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activityId")
    @JsonBackReference
    private Activity activity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seniorId")
    @JsonIgnoreProperties("activityParticipates")
    private Senior senior;

    // Optional: Constructor for date + status only
    public ActivityParticipate(Calendar acparDate, String acparStatus) {
        this.acparDate = acparDate;
        this.acparStatus = acparStatus;
    }
}
