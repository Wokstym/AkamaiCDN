package agh.cs.backendAkamaiCDN.throughput.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "throughput")
public class ThroughputConfig {
    private List<String> sites;
}
