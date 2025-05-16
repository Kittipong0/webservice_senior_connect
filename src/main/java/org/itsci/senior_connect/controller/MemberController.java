package org.itsci.senior_connect.controller;

import org.itsci.senior_connect.model.Member;
import org.itsci.senior_connect.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.core.io.Resource;

import java.nio.file.Path;


@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/register")
    public ResponseObj registerMember(@RequestBody Map<String, String> map) {
        try {
            String memberUserName = map.get("memberUserName");
            String password = map.get("password");
            String memberType = map.get("memberType");
            String memberImage = map.get("memberImage");

            Member member = memberService.registerMember(memberUserName, password, memberType, memberImage);
            if (member != null) {
                return new ResponseObj(HttpStatus.OK.value(), member);
            } else {
                return new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.value(), "ลงทะเบียนไม่สำเร็จ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.value(), "เกิดข้อผิดพลาดในการลงทะเบียน");
        }
    }

    @PostMapping("/login")
    public ResponseObj loginMember(@RequestBody Map<String, String> map) {
        try {
            String memberUserName = map.get("memberUserName");
            String password = map.get("password");

            String loginMessage = memberService.loginMember(memberUserName, password);

            if ("login success".equals(loginMessage)) {
                return new ResponseObj(HttpStatus.OK.value(), "1");
            } else {
                return new ResponseObj(HttpStatus.OK.value(), loginMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.value(), "เกิดข้อผิดพลาดในการเข้าสู่ระบบ");
        }
    }

    @PostMapping("/getprofile")
    public ResponseObj getProfile(@RequestBody Map<String, String> map) {
        try {
            String memberUserName = map.get("memberUserName");
            Member member = memberService.getProfile(memberUserName);

            if (member != null) {
                return new ResponseObj(HttpStatus.OK.value(), member);
            } else {
                return new ResponseObj(HttpStatus.NOT_FOUND.value(), "ไม่พบข้อมูลสมาชิก");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.value(), "การดึงข้อมูลสมาชิกล้มเหลว");
        }
    }

    @PostMapping(value = "/updateprofile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseObj updateProfile(
            @RequestPart("data") String dataJson,
            @RequestPart(value = "memberImage", required = false) MultipartFile file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(dataJson, new TypeReference<Map<String, Object>>() {});
            
            String memberUserName = (String) map.get("memberUserName");
            if (memberUserName == null || memberUserName.isEmpty()) {
                return new ResponseObj(HttpStatus.BAD_REQUEST.value(), "memberUserName ไม่สามารถเว้นว่างได้");
            }

            // ดึงข้อมูลสมาชิกจากฐานข้อมูล เพื่อให้รู้ชื่อไฟล์รูปภาพเดิม
            Member existingMember = memberService.getProfile(memberUserName);
            if (existingMember == null) {
                return new ResponseObj(HttpStatus.NOT_FOUND.value(), "ไม่พบข้อมูลสมาชิก");
            }

            if (file != null && !file.isEmpty()) {
                String originalFileName = file.getOriginalFilename();
                String uploadDir = "E:/web_api/senior_connect/src/main/java/org/itsci/senior_connect/assets/images/";
                File uploadPath = new File(uploadDir);

                if (!uploadPath.exists()) {
                    uploadPath.mkdirs();
                }

                // ลบรูปภาพเก่าถ้ามี
                String oldFileName = existingMember.getMemberImage(); // ดึงจาก DB
                if (oldFileName != null && !oldFileName.isEmpty()) {
                    File oldFile = new File(uploadDir + oldFileName);
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }

                // ตั้งชื่อไฟล์ใหม่หากซ้ำ
                String fileNameWithoutExt;
                String fileExtension = "";
                int dotIndex = originalFileName.lastIndexOf('.');
                if (dotIndex >= 0) {
                    fileNameWithoutExt = originalFileName.substring(0, dotIndex);
                    fileExtension = originalFileName.substring(dotIndex);
                } else {
                    fileNameWithoutExt = originalFileName;
                }

                String newFileName = originalFileName;
                File newFile = new File(uploadDir + newFileName);
                int count = 1;
                while (newFile.exists()) {
                    newFileName = fileNameWithoutExt + "(" + count + ")" + fileExtension;
                    newFile = new File(uploadDir + newFileName);
                    count++;
                }

                try (FileOutputStream fout = new FileOutputStream(newFile)) {
                    fout.write(file.getBytes());
                }

                // ใส่ชื่อไฟล์ใหม่ใน map เพื่ออัปเดตใน DB
                map.put("memberImage", newFileName);
            }

            Member updatedMember = memberService.updateProfile(map);
            return new ResponseObj(HttpStatus.OK.value(), updatedMember);

        } catch (IllegalArgumentException e) {
            return new ResponseObj(HttpStatus.NOT_FOUND.value(), e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.value(), "การอัปเดตข้อมูลสมาชิกล้มเหลว");
        }
    }





    @PostMapping("/deactivateAccount")
    public ResponseObj deactivateAccount(@RequestBody Map<String, String> map) {
        try {
            String memberUserName = map.get("memberUserName");

            boolean success = memberService.deactivateAccount(memberUserName);
            if (success) {
                return new ResponseObj(HttpStatus.OK.value(), "ปิดการใช้งานบัญชีสำเร็จ");
            } else {
                return new ResponseObj(HttpStatus.NOT_FOUND.value(), "ไม่พบข้อมูลสมาชิก");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.value(), "ปิดการใช้งานบัญชีล้มเหลว");
        }
    }

    @GetMapping("/images/{memberImage:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String memberImage) {
        try {
            // ตำแหน่งโฟลเดอร์รูปภาพ
            String uploadDir = "E:/web_api/senior_connect/src/main/java/org/itsci/senior_connect/assets/images/";
            Path filePath = Paths.get(uploadDir).resolve(memberImage).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // ตรวจสอบ Content-Type
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
