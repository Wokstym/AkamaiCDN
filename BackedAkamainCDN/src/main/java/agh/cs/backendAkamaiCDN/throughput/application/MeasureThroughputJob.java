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
import java.util.stream.Collectors;

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
        List<ThroughputTaskComponent> tasks = config.getSites().stream()
                .flatMap(site -> site.getHosts()
                        .stream()
                        .map(host -> ThroughputTaskComponent
                                .from(applicationContext, host, site.getGeneralHost())))
                .collect(Collectors.toList());
        tasks.forEach(task -> log.info(task.host));
        tasks.forEach(executor::execute);

    }

    @Slf4j
    @Component
    @Scope(value = SCOPE_PROTOTYPE)
    @RequiredArgsConstructor(onConstructor = @__(@Autowired), access = AccessLevel.PRIVATE)
    public static class ThroughputTaskComponent implements Runnable {
        private final ThroughputService service;
        private String host;
        private String name;

        public static ThroughputTaskComponent from(ApplicationContext applicationContext,
                                                   String host,
                                                   String name) {
            ThroughputTaskComponent task = applicationContext.getBean(ThroughputTaskComponent.class);
            task.host = host;
            task.name = name;
            return task;
        }

        @Override
        public void run() {
            log.info("Starting task for host: " + host);
            while (true) {
                log.info("Measuring host: " + host);
                service.measureAndSaveThroughput(host, name);
            }
        }
    }
}
