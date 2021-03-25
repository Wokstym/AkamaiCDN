package agh.cs.backendAkamaiCDN.ping.service;

import agh.cs.backendAkamaiCDN.ping.entity.PingEntity;
import agh.cs.backendAkamaiCDN.ping.repository.PingRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PingService {

    private final PingRepository repository;
    private final TcpingResultsService tcpingResultsService;

    public String savePing(String siteName) {
        PingEntity pingEntity = tcpingResultsService.executeTcping(siteName);

        repository.save(pingEntity);
        return "Executed tcping on : " + siteName + " at : " + pingEntity.getId();
    }

    public List<PingEntity> getAll() {
        return repository.findAll();
    }
}
