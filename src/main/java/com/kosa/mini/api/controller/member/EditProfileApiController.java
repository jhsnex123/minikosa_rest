package com.kosa.mini.api.controller.member;

import com.kosa.mini.api.dto.member.MemberEditProfileDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.repository.MemberEditProfileRepository;
import com.kosa.mini.api.repository.MemberRepository;
import com.kosa.mini.api.service.member.MemberEditProfileServiceImpl;
import com.kosa.mini.api.service.member.SignUpService;
import com.kosa.mini.api.service.member.SignUpServiceImpl;
import com.kosa.mini.mvc.domain.store.Store;
import com.kosa.mini.mvc.service.store.StoreService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class EditProfileApiController {
    @Autowired
    private final MemberEditProfileServiceImpl memberEditProfileService; // 서비스 객체 주입
    @Autowired
    private SignUpServiceImpl signUpService;
    @Autowired
    private MemberEditProfileRepository memberEditProfileRepository; // 리퍼지터리 주입


    public EditProfileApiController(MemberEditProfileServiceImpl memberEditProfileService) {
        this.memberEditProfileService = memberEditProfileService;
    }

    // 사용자 정보 조회 (GET)
    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String memberIdStr = userDetails.getUsername();
        Integer memberId;
        try {
            memberId = Integer.parseInt(memberIdStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // 사용자의 정보를 DTO로 반환
        MemberEditProfileDTO memberDTO = memberEditProfileService.getUserInfo(memberId);
        if (memberDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 사용자가 없으면 404 반환
        }

        return ResponseEntity.ok(memberDTO);
    }

    // 사용자 정보 수정 (PUT)
    @PutMapping("/userinfo")
    public ResponseEntity<?> updateUserInfo(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody MemberEditProfileDTO memberEditProfileDTO) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String memberIdStr = userDetails.getUsername();
        Integer memberId;
        try {
            memberId = Integer.parseInt(memberIdStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // 사용자 정보 업데이트
        Member updatedMember = memberEditProfileService.update(memberId, memberEditProfileDTO);

        if (updatedMember == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 잘못된 요청
        }

        // 업데이트된 사용자 정보 반환
        return ResponseEntity.ok(updatedMember);
    }

    // 닉네임 중복 검사
    @GetMapping("/check-editnickname")
    public ResponseEntity<?> checkNickname(@RequestParam("nickname") String nickname) {
        boolean exists = signUpService.isNicknameExists(nickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

}
