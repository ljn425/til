package vue.til;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "vue.til")
@EnableJpaAuditing // JPA Auditing 활성화
public class TilApplication {

	public static void main(String[] args) {
		SpringApplication.run(TilApplication.class, args);
	}

}
