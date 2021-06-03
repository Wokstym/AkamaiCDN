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

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RemoteServerClient {

    private final RestTemplate restTemplate;
    private final RemoteServerEndpoints endpoints;


    public void savePacketLoss(SavePacketLossRequest request) {
        executePost(endpoints.getPacketLossSaveEndpoint(), new HttpEntity<>(request), PacketLossEntity[].class);
    }

    public void saveRTT(SaveRTTRequest request) {
        executePost(endpoints.getRTTSaveEndpoint(), new HttpEntity<>(request), RTTEntity[].class);
    }

    public void saveThroughput(SaveThroughputRequest request) {
        executePost(endpoints.getThroughputSaveEndpoint(), new HttpEntity<>(request), ThroughputEntity.class);
    }

    private <T> void executePost(URI uri, HttpEntity<?> requestEntity, Class<T> responseClass) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, responseClass);
            log.info(response);
            response.getBody();
        } catch (Exception e) {
            log.error(e);
        }
    }
}
