package vue.til.dto;

import lombok.Data;
import vue.til.domain.Member;

import java.time.LocalDateTime;

@Data
public class MemberResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private int v;
    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        this.createdDate = member.getCreatedDate();
        this.lastModifiedDate = member.getLastModifiedDate();
        this.v = member.getV();
    }
}
