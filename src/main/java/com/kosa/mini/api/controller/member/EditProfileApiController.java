package com.kosa.mini.api.controller.member;

import com.kosa.mini.api.dto.member.EditProfileRequest;
import com.kosa.mini.api.dto.member.MemberEditProfileDTO;
import com.kosa.mini.api.dto.member.PasswordCheckDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.service.member.MemberEditProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EditProfileApiController {

    private final MemberEditProfileService memberEditProfileService; // 서비스 객체 주입


    // 사용자 정보 조회 (GET)
    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String memberIdStr = userDetails.getUsername(); //반환값 확인
        log.info("AuthenticationPrincipal memberId: " + memberIdStr);

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
    @PutMapping("/editprofile")
    public ResponseEntity<Member> updateUserInfo(@AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestBody EditProfileRequest request) {

        // 요청 검증
        if (request == null || request.getPasswordCheckDTO() == null) {
            log.warn("Invalid request: passwordCheckDTO is missing.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


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

        MemberEditProfileDTO memberEditProfileDTO = request.getMemberEditProfileDTO();
        PasswordCheckDTO passwordCheckDTO = request.getPasswordCheckDTO();

        // 비밀번호 검증
        if (passwordCheckDTO.getPassword() == null ||
                !passwordCheckDTO.getPassword().equals(passwordCheckDTO.getConfirmPassword())) {
            log.warn("Passwords do not match.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // 사용자 정보 업데이트
        Member updatedMember = memberEditProfileService.update(memberId, memberEditProfileDTO, passwordCheckDTO);

        if (updatedMember == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 잘못된 요청
        }

        // 업데이트된 사용자 정보 반환
        return ResponseEntity.status(HttpStatus.OK).body(updatedMember);
    }

    // 닉네임 중복 검사
    @GetMapping("/check-editnickname")
    public ResponseEntity<?> checkNickname(@RequestParam("nickname") String nickname) {
        boolean exists = memberEditProfileService.isNicknameExists(nickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

}
