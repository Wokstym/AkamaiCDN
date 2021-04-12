package agh.cs.backendAkamaiCDN.common;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cdn")
public class CDNConfig {
    private List<Site> sites;

    @Getter
    @Setter
    public static class Site {
        private String generalHost;
        private List<String> hosts;
    }
}


