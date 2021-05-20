package agh.cs.backendAkamaiCDN.common;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfiguration {

    @Value("${remote.server.url}")
    @Getter(onMethod = @__(@Bean))
    private String remoteServerUrl;

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
