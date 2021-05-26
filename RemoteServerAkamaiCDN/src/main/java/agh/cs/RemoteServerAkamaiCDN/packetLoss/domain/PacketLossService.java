package agh.cs.RemoteServerAkamaiCDN.packetLoss.domain;

import agh.cs.RemoteServerAkamaiCDN.packetLoss.domain.rest.SavePacketLossRequest;
import agh.cs.RemoteServerAkamaiCDN.packetLoss.repository.PacketLossRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PacketLossService {

    private PacketLossRepository repository;

    public List<PacketLossEntity> save(List<SavePacketLossRequest.PacketLossDto> dtos) {

        List<PacketLossEntity> entities = dtos.stream()
                .map(e -> PacketLossEntity.builder()
                        .startDate(e.getStartDate())
                        .endDate(e.getEndDate())
                        .host(e.getHost())
                        .url(e.getUrl())
                        .packetLoss(e.getPacketLoss())
                        .probes(e.getProbes())
                        .interval(e.getInterval())
                        .ipAddress(e.getIpAddress())
                        .build())
                .collect(Collectors.toList());

        return repository.saveAll(entities);
    }

    public List<PacketLossEntity> getAll() {
        return repository.findAll();
    }

    public List<PacketLossEntity> getAllBetweenDates(Date start, Date end) {
        return repository.getAllByStartDateIsAfterAndEndDateIsBeforeOrderByStartDate(start, end);
    }
}
