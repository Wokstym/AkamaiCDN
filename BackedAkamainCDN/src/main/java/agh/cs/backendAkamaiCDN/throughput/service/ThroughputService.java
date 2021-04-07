package agh.cs.backendAkamaiCDN.throughput.service;

import agh.cs.backendAkamaiCDN.throughput.entity.ThroughputEntity;
import agh.cs.backendAkamaiCDN.throughput.repository.ThroughputRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ThroughputService {
    private final ThroughputResultsService service;
    private final ThroughputRepository repository;

    public void measureAndSaveThroughput(String host) {
        log.info("Starting measuring for: " + host);
        Optional<ThroughputEntity> throughputEntity = service.measureThroughput(host);

        if (throughputEntity.isPresent()) {
            ThroughputEntity entity = throughputEntity.get();
            repository.save(entity);
            log.info("Saving throughput: " + entity);
        } else {
            log.error("Server error");
        }
    }

    public List<ThroughputEntity> getAll() {
        return repository.findAll();
    }
}
