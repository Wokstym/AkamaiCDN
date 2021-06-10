package agh.cs.backendAkamaiCDN.ping.application;

import agh.cs.backendAkamaiCDN.common.CDNConfig;
import agh.cs.backendAkamaiCDN.ping.domain.RTTEntity;
import agh.cs.backendAkamaiCDN.ping.domain.RttRepository;
import agh.cs.backendAkamaiCDN.remoteServer.RemoteServerClient;
import agh.cs.backendAkamaiCDN.remoteServer.entity.SaveRTTRequest;
import agh.cs.backendAkamaiCDN.remoteServer.entity.SaveRTTRequest.RTTDto;
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
public class RTTService {
    private final TcpingResultsService tcpingResultsService;
    private final CDNConfig cdnConfig;
    private final RemoteServerClient client;
    private final RttRepository repository;

    public List<RTTEntity> saveRTTEntity(Integer numberOfProbes, Integer interval) {
        List<RTTEntity> rttEntities = cdnConfig.getSites().stream()
                .map(site -> tcpingResultsService.execTcpingForRTT(numberOfProbes, interval, site))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<RTTDto> dtos = rttEntities.stream()
                .map(this::mapRTT)
                .collect(Collectors.toList());

        SaveRTTRequest request = SaveRTTRequest.builder()
                .entities(dtos)
                .build();
        boolean success = client.saveRTT(request);

        if (success) {
            rttEntities = rttEntities.stream().peek(e -> e.setSentToServer(true)).collect(Collectors.toList());
        }

        return repository.saveAll(rttEntities);
    }

    private RTTDto mapRTT(RTTEntity e) {
        return RTTDto.builder()
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
                .build();
    }

    public List<RTTEntity> getAll() {
        return repository.findAll();
    }

    public List<RTTEntity> getAllBetweenDates(Date start, Date end) {
        return repository.getAllByStartDateIsAfterAndEndDateIsBeforeOrderByStartDate(start, end);
    }

    public List<RTTEntity> findAllBySentToServer() {
        return repository.findAllBySentToServer(false);
    }

    public List<RTTDto> mapEntities(List<RTTEntity> rttEntities) {
        return rttEntities.stream().map(this::mapRTT).collect(Collectors.toList());

    }

    public void saveAll(List<RTTEntity> rttEntities) {
        repository.saveAll(rttEntities);

    }
}
