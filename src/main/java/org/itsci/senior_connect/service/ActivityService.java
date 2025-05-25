package org.itsci.senior_connect.service;

import java.util.List;

import org.itsci.senior_connect.model.Activity;
import org.itsci.senior_connect.model.ActivityDTO;
import org.springframework.data.domain.Page;

public interface ActivityService {
    List<Activity> getAllActivities();
    Activity getActivityById(int id);
    List<Activity> getRecommendedActivities(String memberUserName);
    Page<Activity> getActivitiesByPage(int page, int size);
    ActivityDTO convertToDTO(Activity activity);
}
