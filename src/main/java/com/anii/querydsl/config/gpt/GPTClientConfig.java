package com.anii.querydsl.config.gpt;

import com.anii.querydsl.gpt.DefaultGPTClient;
import com.anii.querydsl.gpt.GPTClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

@Configuration
@Slf4j
public class GPTClientConfig {

    @Bean
    public Jackson2JsonDecoder jackson2JsonDecoder(ObjectMapper objectMapper) {
        return new Jackson2JsonDecoder(objectMapper);
    }

    @Bean
    Jackson2JsonEncoder jackson2JsonEncoder(ObjectMapper objectMapper) {
        return new Jackson2JsonEncoder(objectMapper);
    }


    @Bean(name = "gptWebClient")
    public WebClient gptWebClient(ChatGPTProperties properties,
                                  WebClient.Builder builder,
                                  Jackson2JsonEncoder jackson2JsonEncoder,
                                  Jackson2JsonDecoder jackson2JsonDecoder) {
        builder = builder.baseUrl(properties.getHost())
                .filter(logRequest())
                .codecs(clientCodecConfigurer -> {
                    ClientCodecConfigurer.ClientDefaultCodecs clientDefaultCodecs = clientCodecConfigurer.defaultCodecs();
                    clientDefaultCodecs.jackson2JsonDecoder(jackson2JsonDecoder);
                    clientDefaultCodecs.jackson2JsonEncoder(jackson2JsonEncoder);
                })
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
        return builder.clientConnector(connector).build();
    }

    @Bean
    @Qualifier("gptWebClient")
    public GPTClient gptClient(WebClient client) {
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
