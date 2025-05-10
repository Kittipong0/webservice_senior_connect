package org.itsci.senior_connect.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "activitys")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<ActivityParticipate> participants = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "actOwnerId")
    @JsonIgnoreProperties("activitys")
    private ActivityOwner activityOwner;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "typeId")
    @JsonIgnoreProperties("activitys")
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

