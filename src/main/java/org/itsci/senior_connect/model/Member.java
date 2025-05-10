package org.itsci.senior_connect.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    private String memberUserName;

    private String memberPassword;

    private Boolean memberStatus;

    @Temporal(TemporalType.DATE)
    private Calendar memberDate;

    private String memberType;

    private String memberImage;

    private String memberUID;

    // Custom constructor for default values
    public Member(String memberUserName, String memberPassword, String memberType, String memberImage, String memberUID) {
        this.memberUserName = memberUserName;
        this.memberPassword = memberPassword;
        this.memberType = memberType;
        this.memberImage = memberImage;
        this.memberUID = memberUID;
        this.memberStatus = true;
        this.memberDate = Calendar.getInstance();
    }
}
