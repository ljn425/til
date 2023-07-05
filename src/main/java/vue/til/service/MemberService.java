package vue.til.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vue.til.domain.Member;
import vue.til.dto.MemberFormDto;
import vue.til.repository.MemberRepository;

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
}
