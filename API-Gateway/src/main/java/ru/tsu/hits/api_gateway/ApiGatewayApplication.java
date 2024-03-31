package ru.tsu.hits.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/core/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:8081/"))
                .route(p -> p
                        .path("/loan/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:8082/"))
                .route(p -> p
                        .path("/user/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:8083/"))
                .build();
    }
}
