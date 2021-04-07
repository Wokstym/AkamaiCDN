package agh.cs.backendAkamaiCDN.throughput.service;

import agh.cs.backendAkamaiCDN.ping.entity.PingEntity;
import agh.cs.backendAkamaiCDN.throughput.entity.ThroughputEntity;
import agh.cs.backendAkamaiCDN.throughput.repository.ThroughputRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ThroughputService {
    private ThroughputResultsService service;
    private ThroughputRepository repository;

    public String measureAndSaveThroughput(String host){
        Optional<ThroughputEntity> throughputEntity = service.measureThroughput(host);
        throughputEntity.ifPresent(s -> {
            repository.save(s);
            log.info("Saving throughput: " + s);
        });
        return throughputEntity.map(ThroughputEntity::toString).orElse("Server error");
    }

    public List<ThroughputEntity> getAll(){
        return repository.findAll();
    }
}
