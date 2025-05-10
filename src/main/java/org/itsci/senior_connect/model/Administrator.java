package org.itsci.senior_connect.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Administrator extends ActivityOwner {

    @Column(unique = true)
    private String adminName;

    @Column(unique = true)
    private String adminTel;

    @Column(unique = true)
    private String adminEmail;

    // Constructor without relationships (แต่รวมค่าจาก superclass)
    public Administrator(String ownerUsername, String ownerPassword, Boolean ownerStatus, String ownerType,
                         Calendar ownerDate, String ownerImage,
                         String adminName, String adminTel, String adminEmail) {
        super(ownerUsername, ownerPassword, ownerStatus, ownerType, ownerDate, ownerImage);
        this.adminName = adminName;
        this.adminTel = adminTel;
        this.adminEmail = adminEmail;
    }
}
