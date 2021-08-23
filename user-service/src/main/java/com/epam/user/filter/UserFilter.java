package com.epam.user.filter;

import com.epam.user.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Component
@WebFilter(filterName = "UserFilter", urlPatterns = {"/users/"})
@AllArgsConstructor
public class UserFilter implements Filter {
    public static final String PATTERN = "/users/";

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String token = jwtUtil.getToken(request);
        System.out.println(token);

        if (token != null) {
            String requestURI = request.getRequestURI();
            System.out.println(requestURI);

            Integer tokenUserId = (Integer) jwtUtil.getId(token);
            System.out.println(tokenUserId);
            Integer URIUserId = getIdFromURI(request);

            if (!tokenUserId.equals(URIUserId)) {
                System.out.println("WRONG ID!!!");
            }
        }

        chain.doFilter(servletRequest, response);
    }

    private Integer getIdFromURI(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return Integer.parseInt(requestURI.substring(PATTERN.length()));
    }


}
