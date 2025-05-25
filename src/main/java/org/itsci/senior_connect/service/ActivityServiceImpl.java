package org.itsci.senior_connect.service;

import org.itsci.senior_connect.model.Activity;
import org.itsci.senior_connect.model.ActivityDTO;
import org.itsci.senior_connect.model.Administrator;
import org.itsci.senior_connect.model.Organization;
import org.itsci.senior_connect.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityParticipateService activityParticipateService;

    @Override
    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    @Override
    public Activity getActivityById(int id) {
        return activityRepository.findByActivityId(id).orElse(null);
    }

    @Override
    public List<Activity> getRecommendedActivities(String memberUserName) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Activity> getActivitiesByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("activityId").descending());
        return activityRepository.findAll(pageable);
    }

    @Override
    public ActivityDTO convertToDTO(Activity activity) {
        ActivityDTO dto = new ActivityDTO();
        dto.setActivityId(activity.getActivityId());
        dto.setActivityName(activity.getActivityName());
        dto.setActivityLocation(activity.getActivityLocation());
        dto.setActivityStartDate(activity.getActivityStartDate());
        dto.setActivityEndDate(activity.getActivityEndDate());
        dto.setActivityMaxParticipate(activity.getActivityMaxParticipate());
        dto.setActivityDetail(activity.getActivityDetail());
        dto.setActivityStatus(activity.getActivityStatus());
        dto.setActivityImage(activity.getActivityImage());
        int confirmedCount = activityParticipateService.getConfirmedParticipantCount(activity.getActivityId());
        dto.setConfirmedCount(confirmedCount);
        if (activity.getActivityOwner() != null) {
            if (activity.getActivityOwner() instanceof Administrator) {
                dto.setActivityOwnerName(((Administrator) activity.getActivityOwner()).getAdminName());
            } else if (activity.getActivityOwner() instanceof Organization) {
                dto.setActivityOwnerName(((Organization) activity.getActivityOwner()).getOrgName());
            }
        }
        if (activity.getType() != null) {
            dto.setTypeName(activity.getType().getTypeName());
        }
        return dto;
    }

}
