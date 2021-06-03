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

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ThroughputService {
    private final ThroughputResultsService service;
    private final RemoteServerClient client;
    private final ThroughputRepository repository;

    public void measureAndSaveThroughput(String host, String name) {
        Optional<ThroughputEntity> e = service.measureThroughput(host, name);
        e.ifPresent(s -> {
            repository.save(s);
            log.info("Saving throughput");
        });
        e.map(entity -> SaveThroughputRequest.builder()
                .averageValue(entity.getAverageValue())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .maxValue(entity.getMaxValue())
                .minValue(entity.getMinValue())
                .url(entity.getUrl())
                .host(entity.getHost())
                .build())
                .ifPresent(client::saveThroughput);
    }

    public List<ThroughputEntity> getAllBetweenDates(Date start, Date end) {
        log.info(start.toString());
        log.info(end.toString());
        return repository.findAllByStartDateIsAfterAndEndDateIsBeforeOrderByStartDate(start, end);
    }

    public List<ThroughputEntity> getAll() {
        return repository.findAll();
    }
}
