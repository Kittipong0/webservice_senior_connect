package org.itsci.senior_connect.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;

@Entity
@Table(name = "activityParticipates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class,
  property = "acparId"
)
public class ActivityParticipate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer acparId;

    @Temporal(TemporalType.DATE)
    private Calendar acparDate;

    private String acparStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activityId")
    private Activity activity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seniorId")
    private Senior senior;

    // Optional: Constructor for date + status only
    public ActivityParticipate(Calendar acparDate, String acparStatus) {
        this.acparDate = acparDate;
        this.acparStatus = acparStatus;
    }
}
