package agh.cs.backendAkamaiCDN.throughput.application;

import agh.cs.backendAkamaiCDN.common.CDNConfig;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;


@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MeasureThroughputJob {
    private final TaskExecutor executor;
    private final CDNConfig config;
    private final ApplicationContext applicationContext;

    @PostConstruct
    private void run() {
        log.info("Post construct init");
        config.getSites().stream()
                .map(site -> ThroughputTaskComponent.from(applicationContext, site.getHosts(), site.getGeneralHost()))
                .forEach(executor::execute);

    }

    @Slf4j
    @Component
    @Scope(value = SCOPE_PROTOTYPE)
    @RequiredArgsConstructor(onConstructor = @__(@Autowired), access = AccessLevel.PRIVATE)
    public static class ThroughputTaskComponent implements Runnable {
        private final ThroughputService service;
        private List<String> hosts;
        private String name;

        public static ThroughputTaskComponent from(ApplicationContext applicationContext,
                                                   List<String> hosts,
                                                   String name) {
            ThroughputTaskComponent task = applicationContext.getBean(ThroughputTaskComponent.class);
            task.hosts = hosts;
            task.name = name;
            return task;
        }

        @Override
        public void run() {
            log.info("Starting task for host: " + name);
            while (true) {
                service.measureAndSaveThroughput(hosts, name);
            }
        }
    }
}
