package com.company.apiserver.ratelimit;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The simplest possible endpoint - and the target for rate-limit demos.
 *
 * Hammer it and watch your token bucket drain (PowerShell):
 *   1..10 | % { curl.exe -s -o NUL -w "%{http_code}`n" http://localhost:8080/api/ping }
 * -> five 200s (burst capacity), then 429s, then ~1 new 200 per second (refill).
 *
 * Also a template: this is ALL you need for a new endpoint. A returned Map
 * becomes a JSON object automatically.
 */
@RestController
public class PingController {

    @GetMapping("/api/ping")
    public Map<String, Object> ping() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "pong");
        body.put("serverTime", LocalDateTime.now().toString());
        return body;
    }
}
