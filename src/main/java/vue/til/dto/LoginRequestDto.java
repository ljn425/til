package vue.til.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequestDto {

    @Schema(description = "Username", example = "a@a.com")
    private String username;

    @Schema(description = "Password", example = "1234")
    private String password;
}
