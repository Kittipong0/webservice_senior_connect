package org.itsci.senior_connect.controller;

import org.itsci.senior_connect.model.Member;
import org.itsci.senior_connect.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping("/updateprofile")
    public ResponseObj updateProfile(@RequestBody Map<String, Object> map) {
        try {
            String memberUserName = (String) map.get("memberUserName");
            if (memberUserName == null || memberUserName.isEmpty()) {
                return new ResponseObj(HttpStatus.BAD_REQUEST.value(), "memberUserName ไม่สามารถเว้นว่างได้");
            }

            // ส่ง map ตรงไปยัง service เพื่ออัปเดต
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
}
