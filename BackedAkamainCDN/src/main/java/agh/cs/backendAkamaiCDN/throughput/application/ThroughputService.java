package agh.cs.backendAkamaiCDN.throughput.application;

import agh.cs.backendAkamaiCDN.remoteServer.RemoteServerClient;
import agh.cs.backendAkamaiCDN.remoteServer.entity.SaveThroughputRequest;
import agh.cs.backendAkamaiCDN.throughput.domain.ThroughputEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ThroughputService {
    private final ThroughputResultsService service;
    private final RemoteServerClient client;

    public void measureAndSaveThroughput(String host, String name) {
        Optional<ThroughputEntity> optional = service.measureThroughput(host, name);
        optional.map(entity -> SaveThroughputRequest.builder()
                .averageValue(entity.getAverageValue())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .maxValue(entity.getMaxValue())
                .minValue(entity.getMinValue())
                .url(entity.getUrl())
                .host(entity.getHost())
                .build())
                .map(client::saveThroughput)
                .ifPresentOrElse(
                        entity -> log.info("Saving throughput: " + entity),
                        () -> log.error("Server error"));
    }

    public List<ThroughputEntity> getAllBetweenDates(LocalDateTime start, LocalDateTime end) {
        log.info(start.toString());
        log.info(end.toString());
        return client.getAllBetweenDatesThroughput(start, end);
    }

    public List<ThroughputEntity> getAll() {
        return client.getAllThroughput();
    }
}
