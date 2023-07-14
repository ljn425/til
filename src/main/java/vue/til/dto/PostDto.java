package vue.til.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import vue.til.domain.Post;

import java.time.LocalDateTime;

@Data
public class PostDto {
    @Schema(description = "Post id", example = "1")
    private Long id;
    @Schema(description = "Post title", example = "Example Title")
    private String title;
    @Schema(description = "Post content", example = "Example Content")
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public PostDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdDate = post.getCreatedDate();
        this.lastModifiedDate = post.getLastModifiedDate();
    }
}
