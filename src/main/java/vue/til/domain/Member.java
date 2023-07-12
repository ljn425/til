package vue.til.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    private String nickname;
    private int v;

    @Builder
    public Member(Long id, String username, String password, String nickname, LocalDateTime createdAt, LocalDateTime updateAt, int v) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.v = v;
    }
}
