package vue.til.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vue.til.domain.Member;
import vue.til.dto.MemberFormDto;
import vue.til.service.MemberService;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class APIController {

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "회원 정보가 저장됩니다.", tags = { "API Controller" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = Member.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })

    @PostMapping("/signup")
    public ResponseEntity<Member> signup(@RequestBody MemberFormDto memberFormDto){
        System.out.println(memberFormDto);
        Member savedMember = memberService.save(memberFormDto);
        return ResponseEntity.ok(savedMember);
    }
}
