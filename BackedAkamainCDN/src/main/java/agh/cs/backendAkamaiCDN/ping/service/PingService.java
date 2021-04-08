package agh.cs.backendAkamaiCDN.ping.service;

import agh.cs.backendAkamaiCDN.ping.config.PingConfig;
import agh.cs.backendAkamaiCDN.ping.entity.PingEntity;
import agh.cs.backendAkamaiCDN.ping.repository.PingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PingService {

    private final PingRepository repository;
    private final TcpingResultsService tcpingResultsService;
    private final PingConfig pingConfig;

    public List<PingEntity> savePing() {
        return pingConfig.getSites().stream().
                map(tcpingResultsService::executeTcping).
                filter(Optional::isPresent).
                map(Optional::get).
                map(repository::save).
                collect(Collectors.toList());
    }

    public List<PingEntity> getAll() {
        return repository.findAll();
    }

    public List<PingEntity> getAllBetweenDates(Date start, Date end) {
        return repository.getAllByStartDateIsAfterAndEndDateIsBefore(start, end);
    }
}
