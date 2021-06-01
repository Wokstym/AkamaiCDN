package agh.cs.backendAkamaiCDN.remoteServer;


import agh.cs.backendAkamaiCDN.common.GeneralRemoteServerException;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RemoteServerClient {

    private final RestTemplate restTemplate;
    private final RemoteServerEndpoints endpoints;


    public List<PacketLossEntity> savePacketLoss(SavePacketLossRequest request) {
        return Arrays.asList(executePost(endpoints.getPacketLossSaveEndpoint(), new HttpEntity<>(request), PacketLossEntity[].class));
    }

    public List<RTTEntity> saveRTT(SaveRTTRequest request) {
        return Arrays.asList(executePost(endpoints.getRTTSaveEndpoint(), new HttpEntity<>(request), RTTEntity[].class));
    }

    public ThroughputEntity saveThroughput(SaveThroughputRequest request) {
        return executePost(endpoints.getThroughputSaveEndpoint(), new HttpEntity<>(request), ThroughputEntity.class);
    }

    private <T> T executePost(URI uri, HttpEntity<?> requestEntity, Class<T> responseClass) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, responseClass);
            log.info(response);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralRemoteServerException(e);
        }
    }
}
