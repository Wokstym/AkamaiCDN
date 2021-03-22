package agh.cs.backendAkamaiCDN.ping.service;

import agh.cs.backendAkamaiCDN.ping.entity.PingEntity;
import agh.cs.backendAkamaiCDN.ping.repository.PingRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PingService {

    private final PingRepository repository;

    public String savePing() {
        Date date = new Date(System.currentTimeMillis());
        var ping = new PingEntity(date);

        repository.save(ping);
        return String.format("Server time: %s", date);
    }
}
