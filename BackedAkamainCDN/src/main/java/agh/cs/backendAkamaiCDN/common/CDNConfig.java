package agh.cs.backendAkamaiCDN.common;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "CDN")
public class CDNConfig {
    private List<String> sites;
}


