package vue.til.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vue.til.domain.Member;
import vue.til.domain.Post;
import vue.til.dto.PostDto;
import vue.til.dto.PostFormDto;
import vue.til.repository.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final MemberService memberService;
    private final MessageSource messageSource;

    /**
     * 전체 게시글 조회 by username
     * @param username
     * @return 전체 게시글
     */
    public List<Post> findAllByMember(String username) {
        Member member = memberService.findByUsername(username);
        return postRepository.findAllByMemberId(member.getId());
    }

    /**
     * 게시글 저장
     * @param username
     * @param postDto
     * @return 저장된 게시글
     */
    @Transactional
    public Post save(String username, PostFormDto postDto) {
        findByTitle(postDto);
        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .member(memberService.findByUsername(username))
                .build();
        return postRepository.save(post);
    }

    /**
     * 게시글 제목 중복 검사
     * @param postDto
     */
    private void findByTitle(PostFormDto postDto) {
        postRepository.findByTitle(postDto.getTitle())
                .ifPresent(post -> {
                    throw new IllegalArgumentException(messageSource.getMessage("post.duplicate", null, null));
                });
    }

    /**
     * 게시글 삭제
     * @param id 게시글 id
     */
    @Transactional
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    /**
     * 게시글 조회 by id
     * @param id 게시글 id
     * @return 조회된 게시글
     */
    public PostDto findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage("post.notfound", null, null)));
        return new PostDto(post);
    }

    /**
     * 게시글 수정
     * @param id 게시글 id
     * @param postFormDto
     * @return 수정된 게시글
     */
    @Transactional
    public Post update(Long id, PostFormDto postFormDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage("post.notfound", null, null)));
        // 제목이 변경되었을 때 중복 검사
        if (!(post.getTitle().equals(postFormDto.getTitle()))) {
            findByTitle(postFormDto);
        }
        post.changePost(postFormDto);
        return post;
    }
}
