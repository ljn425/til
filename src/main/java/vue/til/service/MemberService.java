package vue.til.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vue.til.domain.Member;
import vue.til.dto.*;
import vue.til.enums.LoginErrorMsg;
import vue.til.repository.MemberRepository;
import vue.til.util.JwtTokenUtil;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    public MemberResponseDto save(MemberFormDto memberFormDto){
        Member member = Member.builder()
                .username(memberFormDto.getUsername())
                .password(passwordEncoder.encode(memberFormDto.getPassword()))
                .nickname(memberFormDto.getNickname())
                .build();
        Member savedMember = memberRepository.save(member);
        return new MemberResponseDto(savedMember);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto){
        LoginResponseDto loginResponseDto = new LoginResponseDto();

        Member findMember = findByUsername(loginRequestDto.getUsername());

        if (findMember == null) {
            // 회원이 존재하지 않을 때
            setLoginResponse(loginResponseDto, false, LoginErrorMsg.INVALID_USERNAME.getMsg(), HttpStatus.UNAUTHORIZED);
        } else {
            if (passwordEncoder.matches(loginRequestDto.getPassword(), findMember.getPassword())) {
                // 로그인 성공
                setLoginResponse(loginResponseDto, true, LoginErrorMsg.SUCCESS.getMsg(), HttpStatus.OK);
                setLoginResponseUser(loginResponseDto, findMember);
                // JWT Token 발행
                String token = jwtTokenUtil.generateToken(findMember.getUsername());
                log.debug("token = {}", token);
                loginResponseDto.setToken(token);
            } else {
                // 비밀번호 불일치
                setLoginResponse(loginResponseDto, false, LoginErrorMsg.INVALID_PASSWORD.getMsg(), HttpStatus.BAD_REQUEST);
            }
        }

        return loginResponseDto;
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    private void setLoginResponse(LoginResponseDto loginResponseDto, boolean success, String message, HttpStatus httpStatus) {
        loginResponseDto.setSuccess(success);
        loginResponseDto.setMessage(message);
        loginResponseDto.setHttpStatus(httpStatus);
    }

    private void setLoginResponseUser(LoginResponseDto loginResponseDto, Member member) {
        LoginResponseDto.UserDto userDto = new LoginResponseDto.UserDto();
        userDto.setUsername(member.getUsername());
        userDto.setNickname(member.getNickname());
        loginResponseDto.setUser(userDto);
    }
}
