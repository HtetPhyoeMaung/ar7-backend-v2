package com.security.spring.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Internal endpoint for update script: reports when the server has had no requests for 2 seconds.
 * Only responds when called from localhost (for security).
 */
@RestController
@RequestMapping("/api/v1/internal")
public class DrainStatusController {

    private static final long IDLE_THRESHOLD_MS = 2_000L;

    private final RequestDrainTracker drainTracker;

    public DrainStatusController(RequestDrainTracker drainTracker) {
        this.drainTracker = drainTracker;
    }

    @GetMapping("/drain-status")
    public ResponseEntity<?> drainStatus(HttpServletRequest request) {
        if (!isLocalhost(request)) {
            return ResponseEntity.status(403).body(Map.of("error", "Only localhost allowed"));
        }
        int active = drainTracker.getActiveRequests();
        long idleMs = drainTracker.getIdleMs();
        boolean readyToShutdown = drainTracker.isIdleForAtLeastMs(IDLE_THRESHOLD_MS);
        return ResponseEntity.ok(Map.of(
                "readyToShutdown", readyToShutdown,
                "activeRequests", active,
                "idleMs", idleMs
        ));
    }

    private static boolean isLocalhost(HttpServletRequest request) {
        String remote = request.getRemoteAddr();
        if (remote == null) return false;
        return "127.0.0.1".equals(remote) || "0:0:0:0:0:0:0:1".equals(remote) || "localhost".equalsIgnoreCase(remote);
    }
}
