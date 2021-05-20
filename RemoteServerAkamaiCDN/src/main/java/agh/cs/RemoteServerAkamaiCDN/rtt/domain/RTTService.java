package agh.cs.RemoteServerAkamaiCDN.rtt.domain;

import agh.cs.RemoteServerAkamaiCDN.packetLoss.domain.PacketLossEntity;
import agh.cs.RemoteServerAkamaiCDN.rtt.domain.rest.SaveRTTRequest;
import agh.cs.RemoteServerAkamaiCDN.rtt.repository.RTTRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RTTService {

    private final RTTRepository repository;

    public List<RTTEntity> save(List<SaveRTTRequest.RTTDto> dtos) {
        List<RTTEntity> entities = dtos.stream()
                .map(e -> RTTEntity.builder()
                        .startDate(e.getStartDate())
                        .endDate(e.getEndDate())
                        .host(e.getHost())
                        .url(e.getUrl())
                        .maxTime(e.getMaxTime())
                        .minTime(e.getMinTime())
                        .averageTime(e.getAverageTime())
                        .standardDeviationTime(e.getStandardDeviationTime())
                        .probes(e.getProbes())
                        .interval(e.getInterval())
                        .build())
                .collect(Collectors.toList());

        return repository.saveAll(entities);
    }

    public List<RTTEntity> getAll() {
        return repository.findAll();
    }

    public List<RTTEntity> getAllBetweenDates(Date start, Date end) {
        return repository.getAllByStartDateIsAfterAndEndDateIsBeforeOrderByStartDate(start, end);
    }
}
