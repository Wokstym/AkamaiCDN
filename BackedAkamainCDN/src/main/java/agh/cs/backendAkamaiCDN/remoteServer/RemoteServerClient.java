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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RemoteServerClient {

    private final RestTemplate restTemplate;
    private final RemoteServerEndpoints endpoints;


    public Optional<List<PacketLossEntity>> savePacketLoss(SavePacketLossRequest request) {
        return execute(endpoints.getPacketLossSaveEndpoint(), HttpMethod.POST, new HttpEntity<>(request), PacketLossEntity[].class).map(Arrays::asList);
    }

    public Optional<List<PacketLossEntity>> getAllPacketLoss() {
        return execute(endpoints.getPacketLossAllEndpoint(), HttpMethod.GET, null, PacketLossEntity[].class).map(Arrays::asList);
    }

    public Optional<List<PacketLossEntity>> getAllBetweenDatesPacketLoss(Date start, Date end) {
        MultiValueMap<String, Object> headers = new LinkedMultiValueMap<>();
        headers.add("startDate", start);
        headers.add("endDate", end);
        return execute(endpoints.getPacketLossEndpoint(), HttpMethod.GET, new HttpEntity<>(headers), PacketLossEntity[].class).map(Arrays::asList);
    }

    public Optional<List<RTTEntity>> saveRTT(SaveRTTRequest request) {
        return execute(endpoints.getRTTSaveEndpoint(), HttpMethod.POST, new HttpEntity<>(request), RTTEntity[].class).map(Arrays::asList);
    }

    public Optional<List<RTTEntity>> getAllRTT() {
        return execute(endpoints.getRTTAllEndpoint(), HttpMethod.GET, null, RTTEntity[].class).map(Arrays::asList);
    }

    public Optional<List<RTTEntity>> getAllBetweenDatesRTT(Date start, Date end) {
        MultiValueMap<String, Object> headers = new LinkedMultiValueMap<>();
        headers.add("startDate", start);
        headers.add("endDate", end);
        return execute(endpoints.getRTTEndpoint(), HttpMethod.GET, new HttpEntity<>(headers), RTTEntity[].class).map(Arrays::asList);
    }

    public Optional<ThroughputEntity> saveThroughput(SaveThroughputRequest request) {
        return execute(endpoints.getThroughputSaveEndpoint(), HttpMethod.POST, new HttpEntity<>(request), ThroughputEntity.class);
    }

    public Optional<List<ThroughputEntity>> getAllThroughput() {
        return execute(endpoints.getThroughputAllEndpoint(), HttpMethod.GET, null, ThroughputEntity[].class).map(Arrays::asList);
    }

    public Optional<List<ThroughputEntity>> getAllBetweenDatesThroughput(Date start, Date end) {
        MultiValueMap<String, Object> headers = new LinkedMultiValueMap<>();
        headers.add("startDate", start);
        headers.add("endDate", end);
        return execute(endpoints.getThroughputEndpoint(), HttpMethod.GET, new HttpEntity<>(headers), ThroughputEntity[].class).map(Arrays::asList);
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
