package agh.cs.backendAkamaiCDN.ping.service;

import agh.cs.backendAkamaiCDN.ping.entity.PingEntity;
import agh.cs.backendAkamaiCDN.ping.repository.PingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PingService {

    private final PingRepository repository;
    private final TcpingResultsService tcpingResultsService;

    public String savePing(String siteName) {
        Optional<PingEntity> pingEntity = tcpingResultsService.executeTcping(siteName);
        pingEntity.ifPresent(s -> {
            repository.save(s);
            log.info("Saving ping: " + s);
        });
        return pingEntity.map(PingEntity::toString).orElse("Server error");
    }

    public List<PingEntity> getAll() {
        return repository.findAll();
    }
}
