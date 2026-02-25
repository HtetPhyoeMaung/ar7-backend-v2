package com.security.spring.config;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tracks in-flight HTTP requests for graceful shutdown.
 * Used to determine when the server has had no requests for a given period (e.g. 2 seconds).
 */
@Component
public class RequestDrainTracker {

    private static final String DRAIN_STATUS_PATH = "/api/v1/internal/drain-status";

    private final AtomicInteger activeRequests = new AtomicInteger(0);
    private volatile long lastRequestFinishedTime = System.currentTimeMillis();

    public void requestStarted(String requestUri) {
        if (requestUri != null && requestUri.startsWith(DRAIN_STATUS_PATH)) {
            return; // do not count polling of drain-status itself
        }
        activeRequests.incrementAndGet();
    }

    public void requestFinished(String requestUri) {
        if (requestUri != null && requestUri.startsWith(DRAIN_STATUS_PATH)) {
            return;
        }
        activeRequests.decrementAndGet();
        lastRequestFinishedTime = System.currentTimeMillis();
    }

    public int getActiveRequests() {
        return activeRequests.get();
    }

    public long getIdleMs() {
        return activeRequests.get() == 0
                ? System.currentTimeMillis() - lastRequestFinishedTime
                : 0;
    }

    /** True when there are no in-flight requests and the last one finished at least idleMs ago. */
    public boolean isIdleForAtLeastMs(long idleMs) {
        return activeRequests.get() == 0 && getIdleMs() >= idleMs;
    }
}
