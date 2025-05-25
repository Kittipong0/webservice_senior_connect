package org.itsci.senior_connect.repository;

import org.itsci.senior_connect.model.Activity;
import org.itsci.senior_connect.model.ActivityOwner;
import org.itsci.senior_connect.model.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    Optional<Activity> findByActivityId(int activityId);
    Optional<Activity> findByActivityName(String activityName);
    Optional<Activity> findByType(ActivityType type);
    Optional<Activity> findByActivityStatus(Boolean activityStatus);
    Optional<Activity> findByActivityOwner(ActivityOwner activityOwner);
}

