package agh.cs.backendAkamaiCDN.remoteServer;


import agh.cs.backendAkamaiCDN.ping.domain.PacketLossEntity;
import agh.cs.backendAkamaiCDN.ping.domain.RTTEntity;
import agh.cs.backendAkamaiCDN.remoteServer.entity.SavePacketLossRequest;
import agh.cs.backendAkamaiCDN.remoteServer.entity.SaveRTTRequest;
import agh.cs.backendAkamaiCDN.remoteServer.entity.SaveThroughputRequest;
import agh.cs.backendAkamaiCDN.throughput.domain.ThroughputEntity;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RemoteServerClient {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private final RestTemplate restTemplate;
    private final RemoteServerEndpoints endpoints;


    public Optional<List<PacketLossEntity>> savePacketLoss(SavePacketLossRequest request) {
        return execute(endpoints.getPacketLossSaveEndpoint(), HttpMethod.POST, new HttpEntity<>(request), PacketLossEntity[].class).map(Arrays::asList);
    }

    public Optional<List<PacketLossEntity>> getAllPacketLoss() {
        return execute(endpoints.getPacketLossAllEndpoint(), HttpMethod.GET, null, PacketLossEntity[].class).map(Arrays::asList);
    }

    public Optional<List<PacketLossEntity>> getAllBetweenDatesPacketLoss(LocalDateTime start, LocalDateTime end) {
        String startDate = start.format(ISO_FORMATTER);
        String endDate = end.format(ISO_FORMATTER);
        return execute(endpoints.getPacketLossEndpoint(startDate, endDate), HttpMethod.GET, null, PacketLossEntity[].class).map(Arrays::asList);
    }

    public Optional<List<RTTEntity>> saveRTT(SaveRTTRequest request) {
        return execute(endpoints.getRTTSaveEndpoint(), HttpMethod.POST, new HttpEntity<>(request), RTTEntity[].class).map(Arrays::asList);
    }

    public Optional<List<RTTEntity>> getAllRTT() {
        return execute(endpoints.getRTTAllEndpoint(), HttpMethod.GET, null, RTTEntity[].class).map(Arrays::asList);
    }

    public Optional<List<RTTEntity>> getAllBetweenDatesRTT(LocalDateTime start, LocalDateTime end) {
        String startDate = start.format(ISO_FORMATTER);
        String endDate = end.format(ISO_FORMATTER);
        return execute(endpoints.getRTTEndpoint(startDate, endDate), HttpMethod.GET, null, RTTEntity[].class).map(Arrays::asList);
    }

    public Optional<ThroughputEntity> saveThroughput(SaveThroughputRequest request) {
        return execute(endpoints.getThroughputSaveEndpoint(), HttpMethod.POST, new HttpEntity<>(request), ThroughputEntity.class);
    }

    public Optional<List<ThroughputEntity>> getAllThroughput() {
        return execute(endpoints.getThroughputAllEndpoint(), HttpMethod.GET, null, ThroughputEntity[].class).map(Arrays::asList);
    }

    public Optional<List<ThroughputEntity>> getAllBetweenDatesThroughput(LocalDateTime start, LocalDateTime end) {
        String startDate = start.format(ISO_FORMATTER);
        String endDate = end.format(ISO_FORMATTER);
        return execute(endpoints.getThroughputEndpoint(startDate, endDate), HttpMethod.GET, null, ThroughputEntity[].class).map(Arrays::asList);
    }

    private <T> Optional<T> execute(URI uri, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseClass) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(uri, method, requestEntity, responseClass);
            log.info(response);
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
