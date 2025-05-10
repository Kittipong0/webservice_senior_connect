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

    @Column(unique = true)
    private String caretakerName;

    private String address;

    @Column(unique = true)
    private String caretakerTel;

    @Column(unique = true)
    private String caretakerEmail;

    @OneToMany(mappedBy = "caretaker", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Senior> seniors = new HashSet<>();

    // Custom constructor: for full member info + caretaker fields
    public Caretaker(String memberUserName, String memberPassword, Boolean memberStatus, Calendar memberDate,
                     String memberType, String memberImage, String memberUID,
                     String caretakerName, String address, String caretakerTel, String caretakerEmail) {
        super(memberUserName, memberPassword, memberStatus, memberDate, memberType, memberImage, memberUID);
        this.caretakerName = caretakerName;
        this.address = address;
        this.caretakerTel = caretakerTel;
        this.caretakerEmail = caretakerEmail;
    }

    // Custom constructor: only caretaker info (for partial use)
    public Caretaker(String caretakerName, String address, String caretakerTel, String caretakerEmail) {
        this.caretakerName = caretakerName;
        this.address = address;
        this.caretakerTel = caretakerTel;
        this.caretakerEmail = caretakerEmail;
    }
}

