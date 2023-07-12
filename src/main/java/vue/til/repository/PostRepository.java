package vue.til.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vue.til.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByMemberId(Long id);
    Optional<Post> findByTitle(String title);
}
