package org.itsci.senior_connect.service;

import org.itsci.senior_connect.model.Caretaker;
import org.itsci.senior_connect.model.Member;
import org.itsci.senior_connect.model.Senior;
import org.itsci.senior_connect.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    private static final String SALT = "123456";

    public Member registerMember(String memberUserName, String password, String memberType, String memberImage, String memberGender) {
        try {
            PasswordUtil util = new PasswordUtil();
            password = util.createPassword(password, SALT);
            Random random = new Random();
            boolean result;
            int memberUID;

            do {
                memberUID = 100000 + random.nextInt(900000);
                result = memberRepository.findByMemberUID(String.valueOf(memberUID)).isPresent();
            } while (result);

            Member member = new Member(memberUserName, password, memberType, memberImage, String.valueOf(memberUID), memberGender);
            return memberRepository.save(member);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String doLoginMember(String memberUserName, String password) {
        try {
            PasswordUtil util = new PasswordUtil();
            password = util.createPassword(password, SALT);
            Optional<Member> memberOptional = memberRepository.findByMemberUserName(memberUserName);
            if (memberOptional.isPresent()) {
                Member member = memberOptional.get();
                if (member.getMemberPassword().equals(password)) {
                    return "login success";
                } else {
                    return "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง";
                }
            }
            return "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง";
        } catch (Exception e) {
            e.printStackTrace();
            return "เกิดขึ้นข้อผิดพลาดในการเข้าสู่ระบบ";
        }
    }

    public String checkStatusMember(String memberUserName) {
        Optional<Member> memberOptional = memberRepository.findByMemberUserName(memberUserName);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            if (member.getMemberStatus()) {
                return "บัญชีใช้งานอยู่";
            } else {
                return "บัญชีถูกปิดการใช้งาน";
            }
        }
        return "ไม่พบสมาชิก";
    }

    public Member getProfile(String memberUserName) {
        Optional<Member> memberOptional = memberRepository.findByMemberUserName(memberUserName);
        return memberOptional.orElse(null);
    }

    public Member updateProfile(Map<String, Object> map) {
        String username = (String) map.get("memberUserName");
        Member member = memberRepository.findByMemberUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบสมาชิก: " + username));

        member.setMemberImage((String) map.getOrDefault("memberImage", member.getMemberImage()));
        member.setMemberUID((String) map.getOrDefault("memberUID", member.getMemberUID()));
        member.setMemberStatus((Boolean) map.getOrDefault("memberStatus", member.getMemberStatus()));
        member.setMemberGender((String) map.getOrDefault("memberGender", member.getMemberGender()));

        if ("ผู้สูงอายุ".equals(member.getMemberType()) && member instanceof Senior) {
            Senior senior = (Senior) member;
            senior.setSeniorName((String) map.getOrDefault("seniorName", senior.getSeniorName()));
            senior.setSeniorTel((String) map.getOrDefault("seniorTel", senior.getSeniorTel()));
            senior.setAddress((String) map.getOrDefault("address", senior.getAddress()));
            senior.setSeniorEmail((String) map.getOrDefault("seniorEmail", senior.getSeniorEmail()));
            senior.setEmergencyName((String) map.getOrDefault("emergencyName", senior.getEmergencyName()));
            senior.setEmergencyRelationship((String) map.getOrDefault("emergencyRelationship", senior.getEmergencyRelationship()));
            senior.setEmergencyContact((String) map.getOrDefault("emergencyContact", senior.getEmergencyContact()));

            if (map.containsKey("seniorDateOfBirth")) {
                try {
                    // คาดว่ามาในรูปแบบ "yyyy-MM-dd"
                    String dateStr = (String) map.get("seniorDateOfBirth");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = sdf.parse(dateStr);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    senior.setSeniorDateOfBirth(calendar);
                } catch (Exception e) {
                    throw new IllegalArgumentException("รูปแบบวันเกิดไม่ถูกต้อง (ต้องเป็น dd/MM/yyyy)");
                }
            }

        } else if ("ผู้ดูแล".equals(member.getMemberType()) && member instanceof Caretaker) {
            Caretaker caretaker = (Caretaker) member;
            caretaker.setCaretakerName((String) map.getOrDefault("caretakerName", caretaker.getCaretakerName()));
            caretaker.setAddress((String) map.getOrDefault("address", caretaker.getAddress()));
            caretaker.setCaretakerTel((String) map.getOrDefault("caretakerTel", caretaker.getCaretakerTel()));
            caretaker.setCaretakerEmail((String) map.getOrDefault("caretakerEmail", caretaker.getCaretakerEmail()));
        }

        return memberRepository.save(member);
    }



    public boolean deactivateAccount(String memberUserName) {
        Optional<Member> memberOptional = memberRepository.findByMemberUserName(memberUserName);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            member.setMemberStatus(false);
            memberRepository.save(member);
            return true;
        }
        return false;
    }
}
