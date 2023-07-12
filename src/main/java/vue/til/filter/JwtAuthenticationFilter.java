package vue.til.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vue.til.dto.ErrorResponseDto;
import vue.til.util.JwtTokenUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;

    private List<String> excludedUrls = Arrays.asList("/api/login", "/api/signup", "/swagger-ui/", "/v3/api-docs/");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            // URI 체크
            String requestUrl = request.getRequestURI();
            log.debug("requestUrl = {}", requestUrl);
            if (isUrlExcluded(requestUrl)) {
                filterChain.doFilter(request, response);
                return;
            }

            // [Header]에서 JWT 받아오기 및 Bearer 제거
            String jwtToken = jwtTokenUtil.getJwtFromRequest(request);
            log.debug("jwtToken = {}", jwtToken);

            // 유효한 토큰인지 확인
            if (jwtToken != null && jwtTokenUtil.validateToken(jwtToken)) {
                log.debug("토큰 유효성 통과");

                // 토큰에서 [username] 가져오기
                String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                log.debug("username = {}", username);

                // [username]으로 [Authentication] 객체 생성
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

                // [SecurityContext]에 [Authentication] 객체 저장
                if (authentication != null) {
                    log.debug("인증성공");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    request.setAttribute("username", username);
                    filterChain.doFilter(request, response);
                }
            } else {
                throw new IllegalArgumentException("Token error");
            }
        } catch (Exception e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());

            ObjectMapper objectMapper = new ObjectMapper();
            String errorResponseDtoJson = objectMapper.writeValueAsString(errorResponseDto);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(errorResponseDtoJson);
        }
    }

    private boolean isUrlExcluded(String url) {
        return excludedUrls.stream().anyMatch(url::startsWith);
    }
}
