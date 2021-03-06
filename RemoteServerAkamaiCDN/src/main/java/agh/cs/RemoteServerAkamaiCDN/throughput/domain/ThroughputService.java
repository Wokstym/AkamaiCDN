package agh.cs.RemoteServerAkamaiCDN.throughput.domain;

import agh.cs.RemoteServerAkamaiCDN.common.Util;
import agh.cs.RemoteServerAkamaiCDN.throughput.domain.rest.SaveThroughputRequest;
import agh.cs.RemoteServerAkamaiCDN.throughput.repository.ThroughputRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ThroughputService {

    private final ThroughputRepository repository;

    public ThroughputEntity save(SaveThroughputRequest dto) {
        return repository.save(map(dto));
    }

    public List<ThroughputEntity> getAllBetweenDates(Date start, Date end) {
        log.info(start.toString());
        log.info(end.toString());
        return repository.findAllByStartDateIsAfterAndEndDateIsBeforeOrderByStartDate(start, end);
    }

    public List<ThroughputEntity> getAll() {
        return repository.findAll();
    }

    public void save(List<SaveThroughputRequest> throughputRequests) {
        repository.saveAll(throughputRequests.stream().map(this::map).collect(Collectors.toList()));
    }

    private ThroughputEntity map(SaveThroughputRequest dto) {
        return ThroughputEntity.builder()
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .maxValue(dto.getMaxValue())
                .minValue(dto.getMinValue())
                .averageValue(dto.getAverageValue())
                .host(dto.getHost())
                .url(dto.getUrl())
                .ipAddress(Util.getIpAddress())
                .build();
    }
}
