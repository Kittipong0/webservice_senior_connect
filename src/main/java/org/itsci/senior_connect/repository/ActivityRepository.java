package org.itsci.senior_connect.repository;

import org.itsci.senior_connect.model.Activity;
import org.itsci.senior_connect.model.ActivityOwner;
import org.itsci.senior_connect.model.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    Optional<Activity> findByActivityId(int activityId);
    Optional<Activity> findByActivityName(String activityName);
    Optional<Activity> findByType(ActivityType type);
    Optional<Activity> findByActivityStatus(Boolean activityStatus);
    Optional<Activity> findByActivityOwner(ActivityOwner activityOwner);
    Page<Activity> findByActivityNameContainingIgnoreCase(String activityName, Pageable pageable);
    List<Activity> findByActivityNameContainingIgnoreCase(String searchQuery);
}

