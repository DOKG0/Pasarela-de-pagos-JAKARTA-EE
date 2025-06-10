package org.tallerjava.moduloMonitoreo.infraestructura;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.influx.InfluxMeterRegistry;
import io.micrometer.influx.InfluxConfig;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Duration;

@ApplicationScoped
public class RegistroMetricasConfig {

    private final MeterRegistry registry;

    public RegistroMetricasConfig() {
        InfluxConfig config = new InfluxConfig() {
            @Override
            public String get(String key) {
                return null;
            }
            @Override
            public String db() {
                return "my-bucket";
            }
            @Override
            public String uri() {
                return "http://localhost:8086";
            }
            @Override
            public String userName() {
                return "admin";
            }
            @Override
            public String password() {
                return "admin123";
            }
            @Override
            public String org() {
                return "my-org";
            }
            @Override
            public String token() {
                return "my-token";
            }
            @Override
            public Duration step() {
                return Duration.ofSeconds(10);
            }
        };
        registry = InfluxMeterRegistry.builder(config).build();
    }

    public MeterRegistry getRegistry() {
        return registry;
    }
}