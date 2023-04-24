package dev.dave.orderservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean //A bean will be created with method name
    @LoadBalanced // Client-side balancing - this will create client side load balancer
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }
}
