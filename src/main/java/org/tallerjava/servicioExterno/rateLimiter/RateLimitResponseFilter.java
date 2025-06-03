package org.tallerjava.servicioExterno.rateLimiter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
public class RateLimitResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        Object available = requestContext.getProperty("rate-limit-restante");
        Object limit = requestContext.getProperty("rate-limit-total");
        Object reset = requestContext.getProperty("rate-limit-reinicio");

        if (available != null) {
            responseContext.getHeaders().add("X-RateLimit-Restante", available.toString());
        }
        if (limit != null) {
            responseContext.getHeaders().add("X-RateLimit-Total", limit.toString());
        }
        if (reset != null) {
            responseContext.getHeaders().add("X-RateLimit-Reinicio-Segundos", reset.toString());
        }
    }
}

