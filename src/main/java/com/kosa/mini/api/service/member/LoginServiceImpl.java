package com.kosa.mini.api.service.member;

import com.kosa.mini.api.dto.member.TokenResponseDTO;
import com.kosa.mini.api.dto.member.LoginDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.RefreshTokenRedis;
import com.kosa.mini.api.exception.LoginException;
import com.kosa.mini.api.repository.MemberRepository;
import com.kosa.mini.api.repository.RefreshTokenRepository;
import com.kosa.mini.api.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginServiceImpl(AuthenticationManager authenticationManager,
                            JwtTokenProvider tokenProvider,
                            RefreshTokenRepository refreshTokenRepository,
                            MemberRepository memberRepository,
                            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public TokenResponseDTO authenticate(LoginDTO loginDTO) throws LoginException {
        try {
            // 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword()
                    )
            );

            // 인증 성공 후 사용자 정보 조회
            Member member = memberRepository.findByEmail(loginDTO.getEmail())
                    .orElseThrow(() -> new LoginException("사용자를 찾을 수 없습니다."));

            // Access Token 및 Refresh Token 생성
            String accessToken = tokenProvider.generateAccessToken(member.getMemberId(), member.getRole().getRoleName());
            String refreshToken = tokenProvider.generateRefreshToken(member.getMemberId());

            // Refresh Token 저장 (Redis, 사용자당 단일 토큰)
            RefreshTokenRedis tokenEntity = RefreshTokenRedis.builder()
                    .memberId(member.getMemberId())
                    .token(refreshToken)
                    .expiryDate(LocalDateTime.now().plusSeconds(tokenProvider.getRefreshTokenExpiration() / 1000))
                    .ttl(tokenProvider.getRefreshTokenExpiration() / 1000) // TTL을 초 단위로 설정
                    .build();
            refreshTokenRepository.save(tokenEntity); // 기존 토큰 덮어쓰기

            log.debug("Refresh Token 저장 성공: {}", refreshToken);

            return TokenResponseDTO.builder()
                    .memberId(member.getMemberId())
                    .name(member.getName())
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .roleId(member.getRole().getRoleId())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception ex) {
            log.error("로그인 실패: ", ex);
            throw new LoginException("로그인에 실패했습니다.", ex);
        }
    }

    @Override
    public TokenResponseDTO refreshToken(String refreshToken) throws LoginException {
        Integer memberId = tokenProvider.getMemberIdFromJWT(refreshToken);
        Optional<RefreshTokenRedis> tokenOpt = refreshTokenRepository.findById(memberId); // memberId 사용

        if (!tokenOpt.isPresent() || !tokenOpt.get().getToken().equals(refreshToken)
                || tokenOpt.get().getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new LoginException("유효하지 않은 Refresh Token입니다.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new LoginException("사용자를 찾을 수 없습니다."));

        // 새로운 Access Token 생성
        String newAccessToken = tokenProvider.generateAccessToken(member.getMemberId(), member.getRole().getRoleName());

        // 새로운 Refresh Token 생성
        String newRefreshToken = tokenProvider.generateRefreshToken(member.getMemberId());

        // 기존 Refresh Token 삭제
        refreshTokenRepository.deleteById(memberId);

        // 새로운 Refresh Token 저장
        RefreshTokenRedis newTokenEntity = RefreshTokenRedis.builder()
                .memberId(member.getMemberId())
                .token(newRefreshToken)
                .expiryDate(LocalDateTime.now().plusSeconds(tokenProvider.getRefreshTokenExpiration() / 1000))
                .ttl(tokenProvider.getRefreshTokenExpiration() / 1000) // TTL을 초 단위로 설정
                .build();
        refreshTokenRepository.save(newTokenEntity);

        log.debug("Refresh Token 갱신 성공: {}", newRefreshToken);

        return TokenResponseDTO.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .roleId(member.getRole().getRoleId())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        Integer memberId = tokenProvider.getMemberIdFromJWT(refreshToken);
        refreshTokenRepository.deleteById(memberId);
        log.debug("Logout 성공: memberId = {}", memberId);
    }

    @Override
    public void saveRefreshToken(String refreshToken, Integer memberId) {
        RefreshTokenRedis tokenEntity = RefreshTokenRedis.builder()
                .memberId(memberId)
                .token(refreshToken)
                .expiryDate(LocalDateTime.now().plusSeconds(tokenProvider.getRefreshTokenExpiration() / 1000))
                .ttl(tokenProvider.getRefreshTokenExpiration() / 1000) // TTL을 초 단위로 설정
                .build();
        refreshTokenRepository.save(tokenEntity);
        log.debug("Refresh Token 저장 성공: {}", refreshToken);
    }
}

