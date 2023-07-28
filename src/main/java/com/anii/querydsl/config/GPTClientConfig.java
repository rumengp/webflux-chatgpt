package com.anii.querydsl.config;

import com.anii.querydsl.gpt.ChatGPTProperties;
import com.anii.querydsl.gpt.DefaultGPTClient;
import com.anii.querydsl.gpt.GPTClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

@Configuration
@Slf4j
public class GPTClientConfig {

    @Bean
    public GPTClient gptClient(ChatGPTProperties properties,
                               WebClient.Builder builder) throws InterruptedException {

        builder = builder.baseUrl(properties.getHost())
                .filter(logRequest())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getToken());
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(properties.getTimeout());
        if (properties.getProxy().getEnable()) {
            httpClient = httpClient.proxy(proxy -> proxy
                    .type(ProxyProvider.Proxy.HTTP)
                    .host(properties.getProxy().getHost())
                    .port(properties.getProxy().getPort()));
        }
        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
        WebClient client = builder.clientConnector(connector).build();

        return new DefaultGPTClient(client);
    }

    // This method returns filter function which will log request data
    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

}
