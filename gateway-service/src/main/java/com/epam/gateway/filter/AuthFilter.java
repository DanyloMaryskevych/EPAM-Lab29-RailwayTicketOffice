package com.epam.gateway.filter;

import com.epam.gateway.util.JwtUtil;
import com.epam.gateway.restObject.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Component
@Slf4j
@AllArgsConstructor
public class AuthFilter implements GlobalFilter {
    private final List<String> apiEndpoints = List.of("/register", "/login");

    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (isApiSecured(request)) {

            if (!request.getHeaders().containsKey("Authorization")) {
                log.info("No Authorization header!");
                throw new UnsupportedOperationException();
            } else {
                log.info("Authentication check!");
                String token = jwtUtil.getToken(request);
                User user = restTemplate.getForObject("http://localhost:8081/users/" + jwtUtil.getId(token),
                        User.class);
                jwtUtil.validateToken(token, user);
                log.info("Authentication by user with ID: " + user.getId() + " successful!");
            }
        }

        log.info("Before chain");
        return chain.filter(exchange);
    }

    private boolean isApiSecured(ServerHttpRequest request) {
        Predicate<ServerHttpRequest> isSecured = r -> apiEndpoints.stream()
                .noneMatch(uri -> r.getURI().getPath().contains(uri));
        return isSecured.test(request);
    }
}
