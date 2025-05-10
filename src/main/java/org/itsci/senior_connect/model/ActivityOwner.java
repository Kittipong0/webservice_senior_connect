package org.itsci.senior_connect.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityOwner {

    @Id
    private String ownerUsername;

    private String ownerPassword;
    private Boolean ownerStatus;
    private String ownerType;

    @Temporal(TemporalType.DATE)
    private Calendar ownerDate;

    private String ownerImage;

    @OneToMany(mappedBy = "activityOwner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("activityowner")
    private Set<Activity> activitys = new HashSet<>();

    // Constructor without relationships
    public ActivityOwner(String ownerUsername, String ownerPassword, Boolean ownerStatus, String ownerType,
                         Calendar ownerDate, String ownerImage) {
        this.ownerUsername = ownerUsername;
        this.ownerPassword = ownerPassword;
        this.ownerStatus = ownerStatus;
        this.ownerType = ownerType;
        this.ownerDate = ownerDate;
        this.ownerImage = ownerImage;
    }

}

