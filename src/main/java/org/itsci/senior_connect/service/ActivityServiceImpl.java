package org.itsci.senior_connect.service;

import org.itsci.senior_connect.model.Activity;
import org.itsci.senior_connect.model.ActivityDTO;
import org.itsci.senior_connect.model.Administrator;
import org.itsci.senior_connect.model.Organization;
import org.itsci.senior_connect.repository.ActivityParticipateRepository;
import org.itsci.senior_connect.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityParticipateService activityParticipateService;

    @Autowired
    private ActivityParticipateRepository activityParticipateRepository;


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
    public Page<ActivityDTO> getActivitiesByPageAndSearchAndSort(int page, int size, String searchQuery, String sortBy) {
        Pageable pageable = PageRequest.of(page, size);
        List<Activity> activities;

        // ค้นหาจากชื่อถ้ามี query
        if (searchQuery == null || searchQuery.isBlank()) {
            activities = activityRepository.findAll();
        } else {
            activities = activityRepository.findByActivityNameContainingIgnoreCase(searchQuery);
        }

        // กรณีเรียงตามความนิยม
        if ("ยอดนิยม".equalsIgnoreCase(sortBy)) {
            List<ActivityDTO> dtos = activities.stream()
                .filter(activity -> {
                    int confirmed = activityParticipateService.getConfirmedParticipantCount(activity.getActivityId());
                    return confirmed < activity.getActivityMaxParticipate();
                })
                .map(activity -> {
                    int confirmed = activityParticipateService.getConfirmedParticipantCount(activity.getActivityId());
                    int pending = activityParticipateRepository.countByActivity_ActivityIdAndAcparStatusIgnoreCase(activity.getActivityId(), "รอการยืนยัน");
                    ActivityDTO dto = convertToDTO(activity);
                    dto.setConfirmedCount(confirmed);
                    return new AbstractMap.SimpleEntry<>(dto, confirmed + pending);
                })
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue())) // มากไปน้อย
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

            int start = Math.min(page * size, dtos.size());
            int end = Math.min(start + size, dtos.size());
            List<ActivityDTO> pageContent = dtos.subList(start, end);

            return new PageImpl<>(pageContent, pageable, dtos.size());
        }

        // กรณี sort ปกติ
        Sort sort;
        switch (sortBy.toLowerCase()) {
            case "ล่าสุด":
                sort = Sort.by("activityStartDate").descending();
                break;
            case "ชื่อ ก-ฮ":
                sort = Sort.by("activityName").ascending();
                break;
            case "ชื่อ ฮ-ก":
                sort = Sort.by("activityName").descending();
                break;
            default:
                sort = Sort.by("activityId").descending();
        }

        pageable = PageRequest.of(page, size, sort);
        Page<Activity> activityPage;

        if (searchQuery == null || searchQuery.isBlank()) {
            activityPage = activityRepository.findAll(pageable);
        } else {
            activityPage = activityRepository.findByActivityNameContainingIgnoreCase(searchQuery, pageable);
        }

        List<ActivityDTO> dtoList = activityPage.getContent().stream()
            .map(activity -> {
                int confirmed = activityParticipateService.getConfirmedParticipantCount(activity.getActivityId());
                ActivityDTO dto = convertToDTO(activity);
                dto.setConfirmedCount(confirmed);
                return dto;
            }).collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, activityPage.getTotalElements());
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

    @Transactional(readOnly = true)
    public Page<ActivityDTO> getActivitiesByPageWithPopularity(int page, int size, String searchQuery) {
        Pageable pageable = PageRequest.of(page, size);
        List<Activity> allActivities = activityRepository.findByActivityNameContainingIgnoreCase(searchQuery);
        List<Activity> notFullActivities = allActivities.stream()
                .filter(activity -> {
                    int confirmed = activityParticipateService.getConfirmedParticipantCount(activity.getActivityId());
                    return confirmed < activity.getActivityMaxParticipate();
                })
                .toList();
        List<ActivityDTO> sortedDtos = notFullActivities.stream()
                .map(activity -> {
                    int confirmed = activityParticipateService.getConfirmedParticipantCount(activity.getActivityId());
                    int pending = activityParticipateRepository
                            .countByActivity_ActivityIdAndAcparStatusIgnoreCase(activity.getActivityId(), "รอการยืนยัน");
                    ActivityDTO dto = convertToDTO(activity);
                    dto.setConfirmedCount(confirmed);
                    int totalParticipants = confirmed + pending;
                    return new AbstractMap.SimpleEntry<>(dto, totalParticipants);
                })
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        int start = Math.min(page * size, sortedDtos.size());
        int end = Math.min(start + size, sortedDtos.size());
        List<ActivityDTO> pageContent = sortedDtos.subList(start, end);
        return new PageImpl<>(pageContent, pageable, sortedDtos.size());
    }

}
