package vue.til.dto;

import lombok.Data;
import vue.til.domain.Post;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostListDto {
    private List<Post> posts;

    public PostListDto(List<Post> posts) {
        this.posts = posts;
    }
}
