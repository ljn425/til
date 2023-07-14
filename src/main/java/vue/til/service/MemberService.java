package vue.til.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vue.til.domain.Member;
import vue.til.dto.LoginRequestDto;
import vue.til.dto.LoginResponseDto;
import vue.til.dto.MemberFormDto;
import vue.til.dto.MemberResponseDto;
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

    private final MessageSource messageSource;

    /**
     * 회원가입
     * @param memberFormDto
     * @return 회원가입된 회원
     */
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

    /**
     * 로그인
     * @param loginRequestDto
     * @return 로그인 결과
     */
    public LoginResponseDto login(LoginRequestDto loginRequestDto){
        LoginResponseDto loginResponseDto = new LoginResponseDto();

        Member findMember = findByUsername(loginRequestDto.getUsername());

        if (findMember == null) {
            // 회원이 존재하지 않을 때
            setLoginResponse(loginResponseDto, false, messageSource.getMessage("login.invalid.username", null, null), HttpStatus.UNAUTHORIZED);
        } else {
            if (passwordEncoder.matches(loginRequestDto.getPassword(), findMember.getPassword())) {
                // 로그인 성공
                setLoginResponse(loginResponseDto, true, messageSource.getMessage("login.success", null, null), HttpStatus.OK);
                setLoginResponseUser(loginResponseDto, findMember);
                // JWT Token 발행
                String token = jwtTokenUtil.generateToken(findMember.getUsername());
                log.debug("token = {}", token);
                loginResponseDto.setToken(token);
            } else {
                // 비밀번호 불일치
                setLoginResponse(loginResponseDto, false, messageSource.getMessage("login.invalid.password", null, null), HttpStatus.BAD_REQUEST);
            }
        }

        return loginResponseDto;
    }

    /**
     * 회원정보 수정
     * @param username
     * @return 수정된 회원정보
     */
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage("login.invalid.username", null, null)));
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
