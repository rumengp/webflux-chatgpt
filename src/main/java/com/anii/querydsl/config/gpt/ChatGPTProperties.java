package com.anii.querydsl.config.gpt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("gpt")
@Data
public class ChatGPTProperties {

    private String host = "https://api.openai.com";

    private String token;

    private Duration timeout = Duration.ofSeconds(5);

    private Proxy proxy = new Proxy();

    @Data
    public static class Proxy {

        private Boolean enable = Boolean.FALSE;

        private String host = "127.0.0.1";

        private Integer port = 7890;
    }

}
