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
    public ResponseObj getActivitiesByPage(@RequestBody Map<String, Object> map) {
        int page = 1;
        int size = 10;
        String sortBy = "ล่าสุด"; // default sort
        String searchQuery = "";
        try {
            if (map.get("page") instanceof Integer) {
                page = (Integer) map.get("page");
            } else if (map.get("page") instanceof String) {
                page = Integer.parseInt((String) map.get("page"));
            }
            if (map.get("size") instanceof Integer) {
                size = (Integer) map.get("size");
            } else if (map.get("size") instanceof String) {
                size = Integer.parseInt((String) map.get("size"));
            }
            if (map.get("sort") instanceof String) {
                sortBy = (String) map.get("sort");
            }
            if (map.get("search") instanceof String) {
                searchQuery = (String) map.get("search");
            }
        } catch (Exception e) {
            return new ResponseObj(400, "ข้อมูลที่ส่งมาไม่ถูกต้อง");
        }
        if (page < 0 || size <= 0) {
            return new ResponseObj(400, "ค่าหน้าและขนาดต้องเป็นค่าบวก");
        }
        try {
            // สมมติ service มี method รับ search และ sort ด้วย
            Page<ActivityDTO> activitiesPage = activityService.getActivitiesByPageAndSearchAndSort(page, size, searchQuery, sortBy);
            List<ActivityDTO> activityDTOs = activitiesPage.getContent();
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

