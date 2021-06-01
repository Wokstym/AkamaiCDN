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
                .map(e -> RTTDto.builder()
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

        SaveRTTRequest request = SaveRTTRequest.builder()
                .entities(dtos)
                .build();
        client.saveRTT(request);
        return repository.saveAll(rttEntities);
    }

    public List<RTTEntity> getAll() {
        return repository.findAll();
    }

    public List<RTTEntity> getAllBetweenDates(Date start, Date end) {
        return repository.getAllByStartDateIsAfterAndEndDateIsBefore(start, end);
    }
}
