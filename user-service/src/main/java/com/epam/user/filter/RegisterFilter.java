package com.epam.user.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@Component
@WebFilter(filterName = "RegisterFilter", urlPatterns = {"/users/register"})
public class RegisterFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        System.out.println("doFilter");
        chain.doFilter(request, response);
    }
}
