package agh.cs.backendAkamaiCDN.common;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestConfiguration {

    @Value("${remote.server.url}")
    @Getter(onMethod = @__(@Bean))
    private String remoteServerUrl;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(100)) // for heroku running app from sleep
                .setReadTimeout(Duration.ofSeconds(100))
                .build();
    }
}
