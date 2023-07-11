package vue.til.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberFormDto {
    private String username;
    private String password;
    private String nickname;
}
