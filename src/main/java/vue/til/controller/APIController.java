package vue.til.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import vue.til.domain.Member;
import vue.til.domain.Post;
import vue.til.dto.*;
import vue.til.service.MemberService;
import vue.til.service.PostService;
import vue.til.util.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class APIController {

    private final MemberService memberService;
    private final PostService postService;

    @Operation(summary = "회원가입", description = "회원 정보가 저장됩니다.", tags = "Signup")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "OK",
//                    content = @Content(schema = @Schema(implementation = Member.class))),
//            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
//            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
//            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
//    })
    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberFormDto memberFormDto){
        return ResponseEntity.ok(memberService.save(memberFormDto));
    }

    @Operation(summary = "로그인", description = "로그인을 합니다.", tags = "Login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = memberService.login(loginRequestDto);


        HttpStatus httpStatus = loginResponseDto.getHttpStatus();
        return ResponseEntity.status(httpStatus).body(loginResponseDto);
    }

    @Operation(summary = "글 조회",description = "작성한 글을 조회합니다.", security = {@SecurityRequirement(name = "bearer-key") }, tags = "Post")
    @GetMapping("/posts")
    public ResponseEntity<PostListDto> posts(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        List<Post> posts = postService.findAllByMember(username);
        PostListDto postListDto = new PostListDto(posts);
        return ResponseEntity.ok(postListDto);
    }

    @Operation(summary = "글 작성", description = "작성한 글을 저장합니다.", security = { @SecurityRequirement(name = "bearer-key") }, tags = "Post")
    @PostMapping("/posts")
    public ResponseEntity<PostDto> posts(HttpServletRequest request, @RequestBody PostFormDto postFormDto) {
        String username = (String) request.getAttribute("username");
        Post post = postService.save(username, postFormDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PostDto(post));
    }

    @Operation(summary = "글 삭제", description = "작성한 글을 삭제합니다.", security = { @SecurityRequirement(name = "bearer-key") }, tags = "Post")
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        log.debug("id = {}", id);
        postService.delete(id);
        return ResponseEntity.ok("success");
    }
}
