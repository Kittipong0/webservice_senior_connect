package org.itsci.senior_connect.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "activitys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class,
  property = "activityId"
)
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer activityId;

    @Column(unique = true)
    private String activityName;

    private String activityLocation;

    @Temporal(TemporalType.DATE)
    private Calendar activityStartDate;

    @Temporal(TemporalType.DATE)
    private Calendar activityEndDate;

    private Integer activityMaxParticipate;

    private String activityDetail;

    private Boolean activityStatus;

    private String activityImage;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ActivityParticipate> participants = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "actOwnerId")
    private ActivityOwner activityOwner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "typeId")
    private ActivityType type;


    // Constructor without ID and relationships
    public Activity(String activityName, String activityLocation, Calendar activityStartDate,
                    Calendar activityEndDate, Integer activityMaxParticipate, String activityDetail,
                    Boolean activityStatus, String activityImage) {
        this.activityName = activityName;
        this.activityLocation = activityLocation;
        this.activityStartDate = activityStartDate;
        this.activityEndDate = activityEndDate;
        this.activityMaxParticipate = activityMaxParticipate;
        this.activityDetail = activityDetail;
        this.activityStatus = activityStatus;
        this.activityImage = activityImage;
    }
}
