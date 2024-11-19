package com.kosa.mini.api.service.member;

import com.kosa.mini.api.dto.member.MemberEditProfileDTO;
import com.kosa.mini.api.dto.member.PasswordCheckDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Role;
import com.kosa.mini.api.entity.Store;
import com.kosa.mini.api.repository.MemberRepository;
import com.kosa.mini.api.repository.RoleRepository;
import com.kosa.mini.api.repository.StoreRepository;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@ToString
@Slf4j
@Service
public class MemberEditProfileServiceImpl implements MemberEditProfileService{

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final StoreRepository storeRepository;

    @Autowired
    public MemberEditProfileServiceImpl(MemberRepository memberRepository,
                                        RoleRepository roleRepository,
                                        PasswordEncoder passwordEncoder,
                                        StoreRepository storeRepository) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.storeRepository = storeRepository;
    }

    // 로그인 중복확인 로직
    public boolean isNicknameExists(String nickname){
        return memberRepository.existsByNickname(nickname);
    }


    // 사용자 정보 조회
    public MemberEditProfileDTO getUserInfo(Integer memberId) {
        // 1. 사용자를 조회
        Member member = memberRepository.findById(memberId).orElse(null);

        if (member == null) {
            log.warn("Member not found for memberId: " + memberId);
            return null; // 사용자가 없으면 null 반환
        }
        log.info("멤버 조회"+member.toString());

        // 2. Member 정보를 DTO로 변환
        MemberEditProfileDTO memberDTO = new MemberEditProfileDTO();
        memberDTO.setMemberId(member.getMemberId());
        memberDTO.setName(member.getName());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setNickname(member.getNickname());
        memberDTO.setPhoneNumber(member.getPhoneNumber());
        memberDTO.setRoleId(member.getRoleId());

        log.info("멤버DTO 조회2"+memberDTO.toString());


        // 3. Role이 사장님(3)인 경우 가게 이름 추가
        if (member.getRole().getRoleId() == 3) { // 사장님
            Store store = storeRepository.findByOwner_MemberId(memberId);
            if (store != null) {
                memberDTO.setStoreName(store.getStoreName());
            }
        }

        // 4.멤버 엔티티를 DTO로 반환
        return memberDTO;
    }

    // 사용자 정보 수정
    public Member update(Integer memberId, MemberEditProfileDTO dto, PasswordCheckDTO passwordCheckDTO) {

        // 방어 로직
        if (passwordCheckDTO == null || passwordCheckDTO.getPassword() == null) {
            log.warn("PasswordCheckDTO is null or password is missing.");
            return null;
        }

        passwordCheckDTO.setPassword(passwordEncoder.encode(passwordCheckDTO.getPassword()));
        Member member = memberRepository.findById(memberId).orElse(null);
        Role role = member.getRole();
        log.info("암호화  pw: " + passwordCheckDTO.getPassword());
        member = dto.toEntity(role);
        log.info("password 인코딩: " + member.getPassword());
        log.info("id: " + memberId + " member: " + member);

        // 1. id 조회
        Member members = memberRepository.findById(memberId).orElse(null);
        if (members == null || !memberId.equals(member.getMemberId())) {
            log.info("잘못된 요청! id : " + memberId + " member: " + member);
            return null;
        }

        // 2. 비밀번호 수정
        if (passwordCheckDTO.getPassword() != null && !passwordCheckDTO.getPassword().isEmpty()) {
            members.setPassword(passwordCheckDTO.getPassword());
        }

        // 3. Role이 사장님(3)인 경우 가게 정보 업데이트
        if (member.getRole().getRoleId() == 3) { // 사장님 Role ID가 3
            Store store = storeRepository.findByOwner_MemberId(memberId);
            if (store != null) {
                store.setStoreName(dto.getStoreName());
                storeRepository.save(store);
            }
        }

         //4. 업데이트 및 저장
        //members.put(member);
        return memberRepository.save(members);
    }

}

