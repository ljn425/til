package vue.til.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class LoginResponseDto {
    private boolean success = false;
    private UserDto user;
    private String message;
    private String token;
    private HttpStatus httpStatus;
    @Data
    public static class UserDto {
        private String username;
        private String nickname;
    }
}