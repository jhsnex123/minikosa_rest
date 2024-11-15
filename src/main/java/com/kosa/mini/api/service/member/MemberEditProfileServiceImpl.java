package com.kosa.mini.api.service.member;

import com.kosa.mini.api.dto.member.MemberEditProfileDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MemberEditProfileServiceImpl {

    @Autowired
    private final MemberRepository memberRepository;


    public MemberEditProfileServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

/*    @Transactional
    public Member update(Integer memberId , MemberEditProfileDTO dto){
        // 1. 정보 조회 및 예외 발생
        Member member = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        log.info("멤버 조회 확인"+member);
        // 2. 계정 수정
        member.getNickname(dto);
        // 3. 엔티티를 DTO로 변환
        Member member1 = dto.toEntity(member);
        // 4. DB 갱신
        Member updated = memberRepository.save(member);
    }*/
//    public MemberEditProfileDTO findByid(Integer memberId){
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 아이디가 없습니다. id= " +memberId));
//            return new MemberEditProfileDTO(member);
//    }

    public Member update(Integer memberId, MemberEditProfileDTO dto) {
        Member member = dto.toEntity();
        log.info("id: "+memberId.toString()+"member: "+member.toString());
        // 2. id 조회하기
        Member members = memberRepository.findById(memberId).orElse(null);
        // 3. 예외 처리
        if(members == null || memberId != member.getMemberId()) {
            //400, 잘못된 요청 응답!
            log.info("잘못된 요청! id : "+memberId.toString()+"member: "+member.toString());
            return null;
        }

        //4. 업데이트 및 정상 응답코드(200)날리기
        members.put(member);
        Member updated = memberRepository.save(members);
        return updated;
    }




    public boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
