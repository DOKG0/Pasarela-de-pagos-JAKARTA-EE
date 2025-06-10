package org.tallerjava.servicioExterno.rateLimiter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.HashMap;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

@Provider
public class RateLimitFilter implements ContainerRequestFilter {

    // Definir limite por endpoint - x/minuto
    private static final Map<String, Integer> LIMITS = new HashMap<>();
    static {
        LIMITS.put("nueva-compra", 50); // 50/minuto
    }
    
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        
        String key = path;
        System.out.println("Path recibido: '" + path + "'");
        int limit = getLimitForPath(path);
        System.out.println("Limite aplicado: " + limit);
        Bucket bucket = buckets.computeIfAbsent(key, k -> createNewBucket(limit));
        
        if (bucket.tryConsume(1)) {
            requestContext.setProperty("rate-limit-restante", bucket.getAvailableTokens());
            requestContext.setProperty("rate-limit-total", limit);
            requestContext.setProperty("rate-limit-reinicio", 60);
            return;
        } else {
            requestContext.abortWith(Response.status(429)
                .entity("Rate limit excedido. Intenta de nuevo mas tarde.")
                .build());
        }
    }
    
    private int getLimitForPath(String path) {
        // SE verificar si es un endpoint de nueva-compra (cualquier comercio)
        if (path.matches("/compra/\\d+/nueva-compra")) {
            return LIMITS.get("nueva-compra");
        }
        
        if (LIMITS.containsKey(path)) {
            return LIMITS.get(path);
        }
        
        return 100;
    }
    
    private Bucket createNewBucket(int limit) {
        Bandwidth bandwidth = Bandwidth.builder()
            .capacity(limit)
            .refillIntervally(limit, Duration.ofMinutes(1))
            .build();
        
        return Bucket.builder()
            .addLimit(bandwidth)
            .build();
    }
}