package vue.til;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@OpenAPIDefinition(
        info = @Info(title = "Vue-til 프로젝트",
                description = "자신이 한 일 기록하는 메모장",
                version = "v1")
)

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig implements WebMvcConfigurer {
    @Bean
    public GroupedOpenApi publicApi() {
        String[] paths = {"/api/**"};
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch(paths)
                .build();
    }
}
