package agh.cs.backendAkamaiCDN.throughput.component;

import agh.cs.backendAkamaiCDN.throughput.service.ThroughputService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Slf4j
@Component
@Scope(value = SCOPE_PROTOTYPE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired), access = AccessLevel.PRIVATE)
public class ThroughputTaskComponent implements Runnable {
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
