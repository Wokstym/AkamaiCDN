package agh.cs.backendAkamaiCDN.throughput.application;

import agh.cs.backendAkamaiCDN.throughput.domain.ThroughputEntity;
import agh.cs.backendAkamaiCDN.throughput.repository.ThroughputRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ThroughputService {
    private final ThroughputResultsService service;
    private final ThroughputRepository repository;

    public void measureAndSaveThroughput(List<String> hosts, String name) {
        log.info("Starting measuring for: " + name);
        Optional<ThroughputEntity> throughputEntity = service.measureThroughput(hosts, name);

        if (throughputEntity.isPresent()) {
            ThroughputEntity entity = throughputEntity.get();
            repository.save(entity);
            log.info("Saving throughput: " + entity);
        } else {
            log.error("Server error");
        }
    }

    public List<ThroughputEntity> getAllBetweenDates(Date start, Date end){
        log.info(start.toString());
        log.info(end.toString());
        return repository.findAllByStartDateIsAfterAndEndDateIsBeforeOrderByStartDate(start, end);
    }

    public List<ThroughputEntity> getAll() {
        return repository.findAll();
    }
}
