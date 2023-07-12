package vue.til.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PostFormDto {
    @Schema(description = "Post title", example = "Example Title")
    private String title;

    @Schema(description = "Post content", example = "Example Content")
    private String content;
}
