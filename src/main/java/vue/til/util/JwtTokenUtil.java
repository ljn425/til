package vue.til.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import vue.til.exception.CustomJwtRuntimeException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtil {
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;// JWT 비밀 키
    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME; // 토큰 만료 시간 (1시간)
    @Value("${jwt.header}")
    private String AUTHORIZATION_HEADER;

    @Value("${jwt.prefix}")
    private String TOKEN_PREFIX;

    public String generateToken(String username) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .setHeaderParam("typ", "JWT")
                .setIssuer("vue.til")
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        return TOKEN_PREFIX + token;
    }

    public boolean validateToken(String token) throws UnsupportedJwtException {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        // 토큰 유효성 확인
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
            throw new CustomJwtRuntimeException(e.getMessage());
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            throw new CustomJwtRuntimeException(e.getMessage());
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            throw new CustomJwtRuntimeException(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            throw new CustomJwtRuntimeException(e.getMessage());
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        System.out.println(bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return bearerToken;
    }


}
