package vue.til.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vue.til.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    
}
