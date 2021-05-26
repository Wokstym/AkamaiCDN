package agh.cs.backendAkamaiCDN.ping.application;

import agh.cs.backendAkamaiCDN.common.CDNConfig;
import agh.cs.backendAkamaiCDN.common.Util;
import agh.cs.backendAkamaiCDN.ping.domain.PacketLossEntity;
import agh.cs.backendAkamaiCDN.remoteServer.RemoteServerClient;
import agh.cs.backendAkamaiCDN.remoteServer.entity.SavePacketLossRequest;
import agh.cs.backendAkamaiCDN.remoteServer.entity.SavePacketLossRequest.PacketLossDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PacketLossService {
    private final RemoteServerClient client;
    private final TcpingResultsService tcpingResultsService;
    private final CDNConfig cdnConfig;

    public List<PacketLossEntity> savePacketLossEntity(Integer numberOfProbes, Integer interval) {
        List<PacketLossDto> dtos = cdnConfig.getSites().stream()
                .map(site -> tcpingResultsService.execTcpingForPacketLoss(numberOfProbes, interval, site))
                .flatMap(Collection::stream)
                .map(e -> PacketLossDto.builder()
                        .startDate(e.getStartDate())
                        .endDate(e.getEndDate())
                        .host(e.getHost())
                        .url(e.getUrl())
                        .packetLoss(e.getPacketLoss())
                        .probes(e.getProbes())
                        .interval(e.getInterval())
                        .build())
                .collect(Collectors.toList());
        SavePacketLossRequest request = SavePacketLossRequest.builder()
                .entities(dtos)
                .build();

        return client.savePacketLoss(request);

    }

    public List<PacketLossEntity> getAll() {
        return client.getAllPacketLoss();
    }

    public List<PacketLossEntity> getAllBetweenDates(LocalDateTime start, LocalDateTime end) {
        return client.getAllBetweenDatesPacketLoss(start, end);
    }
}
