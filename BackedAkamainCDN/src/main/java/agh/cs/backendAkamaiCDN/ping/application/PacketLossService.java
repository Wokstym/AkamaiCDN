package agh.cs.backendAkamaiCDN.ping.application;

import agh.cs.backendAkamaiCDN.common.CDNConfig;
import agh.cs.backendAkamaiCDN.ping.domain.PacketLossEntity;
import agh.cs.backendAkamaiCDN.ping.domain.PacketLossRepository;
import agh.cs.backendAkamaiCDN.remoteServer.RemoteServerClient;
import agh.cs.backendAkamaiCDN.remoteServer.entity.SavePacketLossRequest;
import agh.cs.backendAkamaiCDN.remoteServer.entity.SavePacketLossRequest.PacketLossDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PacketLossService {
    private final RemoteServerClient client;
    private final TcpingResultsService tcpingResultsService;
    private final CDNConfig cdnConfig;
    private final PacketLossRepository repository;

    public List<PacketLossEntity> savePacketLossEntity(Integer numberOfProbes, Integer interval) {
        List<PacketLossEntity> packetLossEntities = cdnConfig.getSites().stream()
                .map(site -> tcpingResultsService.execTcpingForPacketLoss(numberOfProbes, interval, site))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<PacketLossDto> dtos = mapEntities(packetLossEntities);
        SavePacketLossRequest request = SavePacketLossRequest.builder()
                .entities(dtos)
                .build();

        boolean success = client.savePacketLoss(request);

        if (success) {
            packetLossEntities = packetLossEntities.stream().peek(e -> e.setSentToServer(true)).collect(Collectors.toList());
        }

        return repository.saveAll(packetLossEntities);

    }

    public List<PacketLossDto> mapEntities(List<PacketLossEntity> packetLossEntities) {
        return packetLossEntities.stream()
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
    }

    public List<PacketLossEntity> getAll() {
        return repository.findAll();
    }

    public List<PacketLossEntity> getAllBetweenDates(Date start, Date end) {
        return repository.getAllByStartDateIsAfterAndEndDateIsBeforeOrderByStartDate(start, end);
    }

    public List<PacketLossEntity> findAllBySentToServer() {
        return repository.findAllBySentToServer(false);
    }

    public void saveAll(List<PacketLossEntity> packetLossEntities) {
        repository.saveAll(packetLossEntities);

    }
}
