package org.itsci.senior_connect.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organization extends ActivityOwner {

    @Column(unique = true)
    private String orgName;

    private String address;

    @Column(unique = true)
    private String orgTel;

    @Column(unique = true)
    private String orgEmail;

    // Constructor แบบกำหนดค่าทั้งจาก ActivityOwner และ Organization
    public Organization(String ownerUsername, String ownerPassword, Boolean ownerStatus, String ownerType,
                        Calendar ownerDate, String ownerImage,
                        String orgName, String address, String orgTel, String orgEmail) {
        super(ownerUsername, ownerPassword, ownerStatus, ownerType, ownerDate, ownerImage);
        this.orgName = orgName;
        this.address = address;
        this.orgTel = orgTel;
        this.orgEmail = orgEmail;
    }

}
