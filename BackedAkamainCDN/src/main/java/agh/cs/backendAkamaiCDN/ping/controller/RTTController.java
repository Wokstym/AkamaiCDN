package agh.cs.backendAkamaiCDN.ping.controller;

import agh.cs.backendAkamaiCDN.ping.entity.RTTEntity;
import agh.cs.backendAkamaiCDN.ping.service.RTTService;
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
@RequestMapping("/rtt")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RTTController {

    private final RTTService service;

    @GetMapping("/save")
    public ResponseEntity<List<RTTEntity>> pingSite(
            @RequestParam(name = "numberOfProbes", defaultValue = "100") Integer numberOfProbes,
            @RequestParam(name = "interval", defaultValue = "1000") Integer interval
    )
    {
        if (numberOfProbes <= 0 || interval <= 0) return ResponseEntity.badRequest().build();
        log.info("RTT request");
        return ResponseEntity.ok(service.saveRTTEntity(numberOfProbes, interval));
    }

    @GetMapping("/all")
    public ResponseEntity<List<RTTEntity>> getAll() {
        List<RTTEntity> entities = service.getAll();
        if (entities.isEmpty()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("")
    public ResponseEntity<List<RTTEntity>> getAllBetweenDates(
            @RequestParam(name = "start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
            @RequestParam(name = "end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end
    ){
        if (start.after(end)) return ResponseEntity.badRequest().build();
        List<RTTEntity> entities = service.getAllBetweenDates(start, end);
        if (entities.isEmpty()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(entities);
    }
}

