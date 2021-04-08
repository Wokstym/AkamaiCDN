package agh.cs.backendAkamaiCDN.ping.controller;

import agh.cs.backendAkamaiCDN.ping.entity.PingEntity;
import agh.cs.backendAkamaiCDN.ping.service.PingService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
    public List<PingEntity> pingSite() {
        log.info("Ping request");
        return service.savePing();
    }

    @GetMapping("/all")
    public List<PingEntity> getAll() {
        return service.getAll();
    }

    @GetMapping(value = {"/rtt", "/packetLoss"})
    public List<PingEntity> getAllBetweenDates(
            @RequestParam(name = "start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
            @RequestParam(name = "end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end
    ){
        return service.getAllBetweenDates(start, end);
    }
}
