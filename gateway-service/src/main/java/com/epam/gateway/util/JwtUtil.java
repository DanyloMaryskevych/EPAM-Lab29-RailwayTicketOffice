package com.epam.gateway.util;

import com.epam.gateway.restObject.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {

    public static final String AUTH = "Authorization";
    private static final String SECRET_KEY = "secret";

    public String getToken(ServerHttpRequest request) {
        List<String> stringList = request.getHeaders().get(AUTH);
        assert stringList != null;
        return stringList.get(0);
    }

    // Extracting information from token
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private <T> T getSomeClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String getUsername(String token) {
        return getSomeClaim(token, Claims::getSubject);
    }

    public Date getExpiration(String token) {
        return getSomeClaim(token, Claims::getExpiration);
    }

    public Object getId(String token) {
        return getSomeClaim(token, claims -> claims.get("id"));
    }

    // Token Validation
    private Boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, User user) {
        String username = getUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

}
