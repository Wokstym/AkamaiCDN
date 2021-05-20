package agh.cs.backendAkamaiCDN.throughput.application;

import agh.cs.backendAkamaiCDN.common.RemoteServerException;
import agh.cs.backendAkamaiCDN.remoteServer.RemoteServerClient;
import agh.cs.backendAkamaiCDN.remoteServer.entity.SaveThroughputRequest;
import agh.cs.backendAkamaiCDN.throughput.domain.ThroughputEntity;
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

    public void measureAndSaveThroughput(List<String> hosts, String name) {
        log.info("Starting measuring for: " + name);
        service.measureThroughput(hosts, name)
                .map(entity -> SaveThroughputRequest.builder()
                        .averageValue(entity.getAverageValue())
                        .startDate(entity.getStartDate())
                        .endDate(entity.getEndDate())
                        .maxValue(entity.getMaxValue())
                        .minValue(entity.getMinValue())
                        .host(entity.getHost())
                        .build())
                .map(client::saveThroughput)
                .ifPresentOrElse(
                        entity -> log.info("Saving throughput: " + entity),
                        () -> log.error("Server error"));
    }

    public List<ThroughputEntity> getAllBetweenDates(Date start, Date end) {
        log.info(start.toString());
        log.info(end.toString());
        return client.getAllBetweenDatesThroughput(start, end)
                .orElseThrow(RemoteServerException::new);
    }

    public List<ThroughputEntity> getAll() {
        return client.getAllThroughput()
                .orElseThrow(RemoteServerException::new);
    }
}
