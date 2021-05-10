package agh.cs.backendAkamaiCDN.ping.application;

import agh.cs.backendAkamaiCDN.common.CDNConfig;
import agh.cs.backendAkamaiCDN.ping.domain.PacketLossEntity;
import agh.cs.backendAkamaiCDN.ping.repository.PacketLossRepository;
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
    private final PacketLossRepository repository;
    private final TcpingResultsService tcpingResultsService;
    private final CDNConfig cdnConfig;

    public List<PacketLossEntity> savePacketLossEntity(Integer numberOfProbes, Integer interval) {
        return cdnConfig.getSites().stream()
                .map(site -> tcpingResultsService.execTcpingForPacketLoss(numberOfProbes, interval, site))
                .flatMap(Collection::stream)
                .map(repository::save)
                .collect(Collectors.toList());
    }

    public List<PacketLossEntity> getAll() {
        return repository.findAll();
    }

    public List<PacketLossEntity> getAllBetweenDates(Date start, Date end) {
        return repository.getAllByStartDateIsAfterAndEndDateIsBeforeOrderByStartDate(start, end);
    }
}
