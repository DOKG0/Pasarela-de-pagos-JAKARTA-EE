package org.tallerjava.servicioExterno.rateLimiter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

@Provider
public class RateLimitFilter implements ContainerRequestFilter {

    private static final int REQUESTS_PER_MINUTE = 10;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String ip = requestContext.getHeaderString("X-Forwarded-For");
        if (ip == null) {
            ip = requestContext.getUriInfo().getRequestUri().getHost();
        }

        Bucket bucket = buckets.computeIfAbsent(ip, k -> createNewBucket());
        if (bucket.tryConsume(1)) {
            requestContext.setProperty("rate-limit-restante", bucket.getAvailableTokens());
            requestContext.setProperty("rate-limit-total", REQUESTS_PER_MINUTE);
            requestContext.setProperty("rate-limit-reinicio", 60);
            return;
        } else {
            requestContext.abortWith(Response.status(429)
                .entity("Rate limit exceedido. Intenta de nuevo mas tarde.")
                .build());
        }
    }

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.builder()
            .capacity(REQUESTS_PER_MINUTE)
            .refillGreedy(REQUESTS_PER_MINUTE, Duration.ofMinutes(1))
            .build();

        return Bucket.builder().addLimit(limit).build();
    }
}