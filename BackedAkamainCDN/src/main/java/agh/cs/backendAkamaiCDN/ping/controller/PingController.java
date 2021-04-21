package agh.cs.backendAkamaiCDN.ping.controller;

import agh.cs.backendAkamaiCDN.ping.entity.PingEntity;
import agh.cs.backendAkamaiCDN.ping.service.PingService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/ping")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PingController {

    private final PingService service;

    @GetMapping("/save")
    public ResponseEntity<List<PingEntity>> pingSite(
            @RequestParam(name = "numberOfProbes") Integer numberOfProbes,
            @RequestParam(name = "interval") Integer interval
    )
    {
        if (numberOfProbes <= 0 || interval <= 0) return ResponseEntity.badRequest().build();
        log.info("Ping request");
        return ResponseEntity.ok(service.savePing(numberOfProbes, interval));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PingEntity>> getAll() {
        List<PingEntity> entities = service.getAll();
        if (entities.isEmpty()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(entities);
    }

    @GetMapping(value = {"/rtt", "/packetLoss"})
    public ResponseEntity<List<PingEntity>> getAllBetweenDates(
            @RequestParam(name = "start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
            @RequestParam(name = "end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end
    ){
        if (start.after(end)) return ResponseEntity.badRequest().build();
        List<PingEntity> entities = service.getAllBetweenDates(start, end);
        if (entities.isEmpty()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(entities);
    }
}
