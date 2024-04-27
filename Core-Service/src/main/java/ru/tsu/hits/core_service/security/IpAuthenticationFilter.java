package ru.tsu.hits.core_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.*;

public class IpAuthenticationFilter extends GenericFilterBean {

    private static final Set<String> trustedIps = new HashSet<>(Arrays.asList("127.0.0.1", "192.168.1.100"));

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        String remoteIp = httpRequest.getRemoteAddr();
        String serviceName = httpRequest.getHeader("Service-Name");

        if (trustedIps.contains(remoteIp) && Objects.equals(serviceName, "Loan-Service")) {
            Authentication auth = new UsernamePasswordAuthenticationToken("internalService", null, List.of(() -> "ROLE_TRUSTED_SERVICE"));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(req, res);
    }

}
