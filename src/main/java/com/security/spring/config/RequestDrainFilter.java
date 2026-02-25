package com.security.spring.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Counts in-flight requests so we can detect "no request for N seconds" for graceful update.
 */
@Component
@Order(1)
public class RequestDrainFilter extends HttpFilter {

    private final RequestDrainTracker drainTracker;

    public RequestDrainFilter(RequestDrainTracker drainTracker) {
        this.drainTracker = drainTracker;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String uri = request.getRequestURI();
        drainTracker.requestStarted(uri);
        try {
            chain.doFilter(request, response);
        } finally {
            drainTracker.requestFinished(uri);
        }
    }
}
