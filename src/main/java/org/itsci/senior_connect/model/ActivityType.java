package org.itsci.senior_connect.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "activityTypes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer typeId;

    @Column(unique = true)
    private String typeName;

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("activityTypes")
    private Set<Activity> activitys = new HashSet<>();

    // Constructor without ID and relationships
    public ActivityType(String typeName) {
        this.typeName = typeName;
    }
}

