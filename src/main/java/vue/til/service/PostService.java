package vue.til.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vue.til.domain.Member;
import vue.til.domain.Post;
import vue.til.dto.PostDto;
import vue.til.dto.PostFormDto;
import vue.til.repository.MemberRepository;
import vue.til.repository.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final MemberService memberService;

    public List<Post> findAllByMember(String username) {
        Member member = memberService.findByUsername(username);
        return postRepository.findAllByMemberId(member.getId());
    }
    @Transactional
    public Post save(String username, PostFormDto postDto) {
        postRepository.findByTitle(postDto.getTitle())
                .ifPresent(post -> {
                    throw new IllegalArgumentException("이미 존재하는 제목입니다.");
                });
        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .member(memberService.findByUsername(username))
                .build();
        return postRepository.save(post);
    }
}
