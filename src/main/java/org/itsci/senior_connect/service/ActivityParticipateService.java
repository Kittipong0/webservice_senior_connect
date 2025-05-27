package org.itsci.senior_connect.service;

import org.itsci.senior_connect.repository.ActivityParticipateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityParticipateService {

    @Autowired
    private ActivityParticipateRepository activityParticipateRepository;

    public int getConfirmedParticipantCount(Integer activityId) {
        return activityParticipateRepository
                .countByActivity_ActivityIdAndAcparStatusIgnoreCase(activityId, "ยืนยันแล้ว");
    }
}
