package org.itsci.senior_connect.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Caretaker extends Member {

    private String caretakerName;

    private String address;

    @Column(unique = true)
    private String caretakerTel;

    @Temporal(TemporalType.DATE)
    private Calendar caretakerDateOfBirth;

    @Column(unique = true)
    private String caretakerEmail;

    @OneToMany(mappedBy = "caretaker", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Senior> seniors = new HashSet<>();

    // Custom constructor: for full member info + caretaker fields
    public Caretaker(String memberUserName, String memberPassword, Boolean memberStatus, Calendar memberDate,
                     String memberType, String memberImage, String memberUID, String memberGender,
                     String caretakerName, String address, String caretakerTel, Calendar caretakerDateOfBirth, String caretakerEmail) {
        super(memberUserName, memberPassword, memberStatus, memberDate, memberType, memberImage, memberUID, memberGender);
        this.caretakerName = caretakerName;
        this.address = address;
        this.caretakerTel = caretakerTel;
        this.caretakerDateOfBirth = caretakerDateOfBirth;
        this.caretakerEmail = caretakerEmail;
    }

    // Custom constructor: only caretaker info (for partial use)
    public Caretaker(String caretakerName, String address, String caretakerTel, Calendar caretakerDateOfBirth, String caretakerEmail) {
        this.caretakerName = caretakerName;
        this.address = address;
        this.caretakerTel = caretakerTel;
        this.caretakerDateOfBirth = caretakerDateOfBirth;
        this.caretakerEmail = caretakerEmail;
    }
}

