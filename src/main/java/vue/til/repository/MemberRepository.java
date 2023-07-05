package vue.til.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vue.til.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
