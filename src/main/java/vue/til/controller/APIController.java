package vue.til.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vue.til.domain.Member;
import vue.til.dto.MemberFormDto;
import vue.til.service.MemberService;

@RestController
@AllArgsConstructor
public class APIController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Member> signup(@RequestBody MemberFormDto memberFormDto){
        System.out.println(memberFormDto);
        Member savedMember = memberService.save(memberFormDto);
        return ResponseEntity.ok(savedMember);
    }
}
