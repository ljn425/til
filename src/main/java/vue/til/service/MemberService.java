package vue.til.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vue.til.domain.Member;
import vue.til.dto.LoginRequestDto;
import vue.til.dto.LoginResponseDto;
import vue.til.dto.MemberFormDto;
import vue.til.enums.LoginErrorMsg;
import vue.til.repository.MemberRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member save(MemberFormDto memberFormDto){
        Member member = Member.builder()
                .username(memberFormDto.getUsername())
                .password(passwordEncoder.encode(memberFormDto.getPassword()))
                .nickname(memberFormDto.getNickname())
                .build();
        return memberRepository.save(member);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto){
        LoginResponseDto loginResponseDto = new LoginResponseDto();

        Member findMember = memberRepository.findByUsername(loginRequestDto.getUsername()).orElse(null);

        if (findMember == null) {
            // 회원이 존재하지 않을 때
            setLoginResponse(loginResponseDto, false, LoginErrorMsg.INVALID_USERNAME.getMsg(), HttpStatus.UNAUTHORIZED);
        } else {
            if (passwordEncoder.matches(loginRequestDto.getPassword(), findMember.getPassword())) {
                // 로그인 성공
                setLoginResponse(loginResponseDto, true, LoginErrorMsg.SUCCESS.getMsg(), HttpStatus.OK);
                setLoginResponseUser(loginResponseDto, findMember);
                // 토큰 발급
                loginResponseDto.setToken(UUID.randomUUID().toString());
            } else {
                // 비밀번호 불일치
                setLoginResponse(loginResponseDto, false, LoginErrorMsg.INVALID_PASSWORD.getMsg(), HttpStatus.BAD_REQUEST);
            }
        }

        return loginResponseDto;
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
