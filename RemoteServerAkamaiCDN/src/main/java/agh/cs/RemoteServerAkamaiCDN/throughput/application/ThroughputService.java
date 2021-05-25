package agh.cs.RemoteServerAkamaiCDN.throughput.application;

import agh.cs.RemoteServerAkamaiCDN.throughput.domain.ThroughputEntity;
import agh.cs.RemoteServerAkamaiCDN.throughput.domain.rest.SaveThroughputRequest;
import agh.cs.RemoteServerAkamaiCDN.throughput.repository.ThroughputRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ThroughputService {

    private final ThroughputRepository repository;

    public ThroughputEntity save(SaveThroughputRequest dto) {
        return repository.save(ThroughputEntity.builder()
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .maxValue(dto.getMaxValue())
                .minValue(dto.getMinValue())
                .averageValue(dto.getAverageValue())
                .host(dto.getHost())
                .build());
    }

    public List<ThroughputEntity> getAllBetweenDates(Date start, Date end){
        log.info(start.toString());
        log.info(end.toString());
        return repository.findAllByStartDateIsAfterAndEndDateIsBeforeOrderByStartDate(start, end);
    }

    public List<ThroughputEntity> getAll() {
        return repository.findAll();
    }
}
