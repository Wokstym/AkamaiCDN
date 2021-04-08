package agh.cs.backendAkamaiCDN.throughput.component;

import agh.cs.backendAkamaiCDN.common.CDNConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ThroughputComponent {
    private final TaskExecutor executor;
    private final CDNConfig config;
    private final ApplicationContext applicationContext;

    @PostConstruct
    private void runThroughputTest() {
        log.info("Post construct init, sites: " + config.getSites().stream().reduce((sum, el) -> sum + " " + el).orElse(""));
        config.getSites().stream()
                .map(host -> ThroughputTaskComponent.from(applicationContext, host))
                .forEach(executor::execute);
    }
}
