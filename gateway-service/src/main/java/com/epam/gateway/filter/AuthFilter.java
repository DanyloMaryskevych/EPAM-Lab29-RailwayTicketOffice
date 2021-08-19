package com.epam.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Component
@Slf4j
public class AuthFilter implements GlobalFilter {
    private final List<String> apiEndpoints = List.of("/register", "/login");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("******************  Global Pre Filter executed");
        ServerHttpRequest request = exchange.getRequest();

        if (isApiSecured(request)) {

            if (!request.getHeaders().containsKey("Authorization")) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                Exception error = new UnsupportedOperationException();
                return response.writeWith(Mono.error(error));
            }
        }

        System.out.println("Before chain");
        return chain.filter(exchange);
    }

    private boolean isApiSecured(ServerHttpRequest request) {
        Predicate<ServerHttpRequest> isSecured = r -> apiEndpoints.stream()
                .anyMatch(uri -> r.getURI().getPath().contains(uri));
        return isSecured.test(request);
    }
}
