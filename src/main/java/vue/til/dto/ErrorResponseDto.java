package vue.til.dto;

import lombok.Data;

@Data
public class ErrorResponseDto {
    private int status;
    private String message;
}
