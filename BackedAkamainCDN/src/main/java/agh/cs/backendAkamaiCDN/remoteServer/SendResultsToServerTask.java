package agh.cs.backendAkamaiCDN.remoteServer;

import agh.cs.backendAkamaiCDN.ping.application.PacketLossService;
import agh.cs.backendAkamaiCDN.ping.application.RTTService;
import agh.cs.backendAkamaiCDN.ping.domain.PacketLossEntity;
import agh.cs.backendAkamaiCDN.ping.domain.RTTEntity;
import agh.cs.backendAkamaiCDN.remoteServer.entity.BatchedRequest;
import agh.cs.backendAkamaiCDN.throughput.application.ThroughputService;
import agh.cs.backendAkamaiCDN.throughput.domain.ThroughputEntity;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class SendResultsToServerTask {

    private final RemoteServerClient client;
    private final PacketLossService packetLossService;
    private final RTTService rttService;
    private final ThroughputService throughputService;

    @Scheduled(cron = "0 0 0/1 1/1 * ? *")
    public void reportCurrentTime() {
        List<PacketLossEntity> packetLossEntities = packetLossService.findAllBySentToServer();
        List<RTTEntity> rttEntities = rttService.findAllBySentToServer();
        List<ThroughputEntity> throughputEntities = throughputService.findAllBySentToServer();

        if (packetLossEntities.isEmpty() && rttEntities.isEmpty() && throughputEntities.isEmpty()) {
            return;
        }


        BatchedRequest request = BatchedRequest.builder()
                .packetLossDtos(packetLossService.mapEntities(packetLossEntities))
                .rttDtos(rttService.mapEntities(rttEntities))
                .throughputRequests(throughputService.mapEntities(throughputEntities))
                .build();

        boolean success = client.sendBatched(request);

        if (success) {
            packetLossEntities = packetLossEntities.stream().peek(e -> e.setSentToServer(true)).collect(Collectors.toList());
            rttEntities = rttEntities.stream().peek(e -> e.setSentToServer(true)).collect(Collectors.toList());
            throughputEntities = throughputEntities.stream().peek(e -> e.setSentToServer(true)).collect(Collectors.toList());

            packetLossService.saveAll(packetLossEntities);
            rttService.saveAll(rttEntities);
            throughputService.saveAll(throughputEntities);
        }

    }

}
