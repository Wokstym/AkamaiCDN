package agh.cs.backendAkamaiCDN.ping.web;

import agh.cs.backendAkamaiCDN.ping.application.RTTService;
import agh.cs.backendAkamaiCDN.ping.domain.RTTEntity;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Validated
@RestController
@RequestMapping("/rtt")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RTTController {

    private final RTTService service;

    @GetMapping("/save")
    public ResponseEntity<List<RTTEntity>> pingSite(
            @RequestParam(name = "numberOfProbes", defaultValue = "100")
            @Positive
                    Integer numberOfProbes,
            @RequestParam(name = "interval", defaultValue = "1000")
            @Positive
                    Integer interval) {
        log.info("RTT request");
        return ResponseEntity.ok(service.saveRTTEntity(numberOfProbes, interval));
    }

    @GetMapping("/all")
    public ResponseEntity<List<RTTEntity>> getAll() {
        List<RTTEntity> entities = service.getAll();
        if (entities.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(entities);
    }

    @GetMapping
    public ResponseEntity<List<RTTEntity>> getAllBetweenDates(
            @RequestParam(name = "startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime start,
            @RequestParam(name = "endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime end) {
        if (start.isAfter(end))
            return ResponseEntity.badRequest().build();

        List<RTTEntity> entities = service.getAllBetweenDates(start, end);
        if (entities.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(entities);
    }
}

