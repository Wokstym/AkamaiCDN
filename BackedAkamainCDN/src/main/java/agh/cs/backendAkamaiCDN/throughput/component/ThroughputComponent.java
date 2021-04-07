package agh.cs.backendAkamaiCDN.throughput.component;

import agh.cs.backendAkamaiCDN.throughput.service.ThroughputService;
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
    private final ThroughputService service;
    private final ApplicationContext context;
    private final TaskExecutor executor;

    @PostConstruct
    private void runThroughputTest(){
        log.info("test");
        while(true){
            service.measureAndSaveThroughput("facebook.com");
        }
    }
}
