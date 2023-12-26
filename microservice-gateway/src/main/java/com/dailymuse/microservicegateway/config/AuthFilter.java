package com.dailymuse.microservicegateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (
                    !exchange
                            .getRequest()
                            .getHeaders()
                            .containsKey(HttpHeaders.AUTHORIZATION)
            )
                throw new RuntimeException("Missing auth information in header");

            String authHeader = Objects
                    .requireNonNull(
                        exchange
                            .getRequest()
                            .getHeaders()
                            .get(HttpHeaders.AUTHORIZATION)
                    )
                    .get(0);

            String[] parts = authHeader.split(" ");

            if (parts.length != 2 || !"Bearer".equals(parts[0])) {
                throw new RuntimeException("Incorrect auth header structure");
            }

            return webClientBuilder.build()
                    .post()
                    .uri("http://localhost:8090/api/auth/validate?token=" + parts[1])
                    .retrieve().bodyToMono(Long.class)
                    .map(id -> {
                        exchange.getRequest()
                                .mutate()
                                .header("x-user-id", String.valueOf(id));
                        return exchange;
                    }).flatMap(chain::filter);
        };
    }

    public static class Config {

    }
}
