package agh.cs.backendAkamaiCDN.throughput.application;

import agh.cs.backendAkamaiCDN.remoteServer.RemoteServerClient;
import agh.cs.backendAkamaiCDN.remoteServer.entity.SaveThroughputRequest;
import agh.cs.backendAkamaiCDN.throughput.domain.ThroughputEntity;
import agh.cs.backendAkamaiCDN.throughput.domain.ThroughputRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ThroughputService {
    private final ThroughputResultsService service;
    private final RemoteServerClient client;
    private final ThroughputRepository repository;

    public void measureAndSaveThroughput(String host, String name) {
        Optional<ThroughputEntity> e = service.measureThroughput(host, name);
        boolean success = e.map(this::map)
                .map(client::saveThroughput)
                .orElse(false);

        e.ifPresent(s -> {
            s.setSentToServer(success);
            repository.save(s);
            log.info("Saving throughput");
        });
    }

    private SaveThroughputRequest map(ThroughputEntity entity) {
        return SaveThroughputRequest.builder()
                .averageValue(entity.getAverageValue())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .maxValue(entity.getMaxValue())
                .minValue(entity.getMinValue())
                .url(entity.getUrl())
                .host(entity.getHost())
                .build();
    }

    public List<ThroughputEntity> getAllBetweenDates(Date start, Date end) {
        log.info(start.toString());
        log.info(end.toString());
        return repository.findAllByStartDateIsAfterAndEndDateIsBeforeOrderByStartDate(start, end);
    }

    public List<ThroughputEntity> getAll() {
        return repository.findAll();
    }

    public List<ThroughputEntity> findAllBySentToServer() {
        return repository.findAllBySentToServer(false);
    }

    public List<SaveThroughputRequest> mapEntities(List<ThroughputEntity> throughputEntities) {
        return throughputEntities.stream().map(this::map).collect(Collectors.toList());
    }

    public void saveAll(List<ThroughputEntity> throughputEntities) {
        repository.saveAll(throughputEntities);
    }
}
