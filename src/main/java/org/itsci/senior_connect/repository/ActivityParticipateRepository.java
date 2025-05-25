package org.itsci.senior_connect.repository;

import org.itsci.senior_connect.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityParticipateRepository extends JpaRepository<ActivityParticipate, Integer> {

    int countByActivity_ActivityIdAndAcparStatusIgnoreCase(Integer activityId, String acparStatus);
}
