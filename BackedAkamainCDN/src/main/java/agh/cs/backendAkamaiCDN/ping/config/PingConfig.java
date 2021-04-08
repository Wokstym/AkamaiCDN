package agh.cs.backendAkamaiCDN.ping.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "ping")
public class PingConfig {
    private List<String> sites;
}
