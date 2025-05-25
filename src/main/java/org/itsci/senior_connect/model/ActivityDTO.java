package org.itsci.senior_connect.model;

import lombok.*;
import java.util.Calendar;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDTO {
    private Integer activityId;
    private String activityName;
    private String activityLocation;
    private Calendar activityStartDate;
    private Calendar activityEndDate;
    private Integer activityMaxParticipate;
    private String activityDetail;
    private Boolean activityStatus;
    private String activityImage;
    private String activityOwnerName;
    private String typeName;
    private int confirmedCount;
}


