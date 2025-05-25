package org.itsci.senior_connect.controller;

import org.itsci.senior_connect.model.Activity;
import org.itsci.senior_connect.model.ActivityDTO;
import org.itsci.senior_connect.service.ActivityService;
import org.itsci.senior_connect.service.ActivityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.itsci.senior_connect.repository.ActivityRepository;
import org.springframework.core.io.Resource;

import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityServiceImpl activityServiceImpl;

    @PostMapping("/getActivity")
    public ResponseObj getActivitiesByPage(@RequestBody Map<String, Integer> map) {
        int page = map.getOrDefault("page", 0);
        int size = map.getOrDefault("size", 10);
        if (page < 0 || size <= 0) {
            return new ResponseObj(400, "ค่าหน้าและขนาดต้องเป็นค่าบวก");
        }
        try {
            List<Activity> allActivities = activityService.getAllActivities();
            if (allActivities.size() <= 10) {
                List<ActivityDTO> dtos = allActivities.stream()
                        .map(activity -> activityServiceImpl.convertToDTO(activity))
                        .toList();
                return new ResponseObj(200, dtos);
            }
            Page<Activity> activitiesPage = activityService.getActivitiesByPage(page, size);
            List<ActivityDTO> activityDTOs = activitiesPage.getContent().stream()
                    .map(activity -> activityServiceImpl.convertToDTO(activity))
                    .toList();
            return new ResponseObj(200, activityDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObj(500, "เกิดข้อผิดพลาดในการดึงข้อมูลกิจกรรม");
        }
    }

    @PostMapping("/getActivityDetail")
    public ResponseObj getActivityDetail(@RequestBody Map<String, String> map) {
        try {
            if (!map.containsKey("activityId")) {
                return new ResponseObj(400, "กรุณาระบุ activityId");
            }
            int id = Integer.parseInt(map.get("activityId"));
            Activity activity = activityService.getActivityById(id);
            if (activity != null) {
                ActivityDTO dto = activityServiceImpl.convertToDTO(activity);
                return new ResponseObj(200, dto);
            } else {
                return new ResponseObj(200, "ไม่พบกิจกรรมที่ระบุ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObj(500, "เกิดข้อผิดพลาดในการดึงข้อมูลกิจกรรม");
        }
    }


    @PostMapping(value = "/getRecommendActivity", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObj getRecommendedActivities(@RequestBody Map<String, String> map) {
        try {
            String memberUserName = map.get("memberUserName");
            List<Activity> activities = activityService.getRecommendedActivities(memberUserName);
            return new ResponseObj(200, activities);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObj(500, null);
        }
    }

    @GetMapping("/images/{activityId}")
    public ResponseEntity<Resource> getImageByActivityId(@PathVariable int activityId) {
        try {
            Optional<Activity> optionalActivity = activityRepository.findByActivityId(activityId);
            if (optionalActivity.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Activity activity = optionalActivity.get();
            String imageFileName = activity.getActivityImage();
            String uploadDir = "E:/web_api/senior_connect/src/main/java/org/itsci/senior_connect/assets/images/";
            Path filePath = Paths.get(uploadDir).resolve(imageFileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

