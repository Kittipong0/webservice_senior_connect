package org.itsci.senior_connect.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Senior extends Member {

    @Column(unique = true)
    private String seniorName;

    @Column(unique = true)
    private String seniorTel;

    private String address;

    @Column(unique = true)
    private String seniorEmail;

    private Calendar seniorDateOfBirth;

    private String emergencyName;

    private String emergencyRelationship;

    private String emergencyContact;

    @OneToMany(mappedBy = "senior", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<ActivityParticipate> activityParticipates = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "caretakerId")
    @JsonBackReference
    private Caretaker caretaker;

    // Custom constructor without activityParticipates & caretaker
    public Senior(String memberUserName, String memberPassword, Boolean memberStatus, Calendar memberDate,
            String memberType, String memberImage, String memberUID, String seniorName, String seniorTel,
            String address, String seniorEmail, Calendar seniorDateOfBirth, String emergencyName,
            String emergencyRelationship, String emergencyContact) {
        super(memberUserName, memberPassword, memberStatus, memberDate, memberType, memberImage, memberUID);
        this.seniorName = seniorName;
        this.seniorTel = seniorTel;
        this.address = address;
        this.seniorEmail = seniorEmail;
        this.seniorDateOfBirth = seniorDateOfBirth;
        this.emergencyName = emergencyName;
        this.emergencyRelationship = emergencyRelationship;
        this.emergencyContact = emergencyContact;
    }
}
