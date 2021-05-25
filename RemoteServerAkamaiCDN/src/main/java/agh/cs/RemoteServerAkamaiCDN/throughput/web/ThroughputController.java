package agh.cs.RemoteServerAkamaiCDN.throughput.web;

import agh.cs.RemoteServerAkamaiCDN.rtt.domain.RTTEntity;
import agh.cs.RemoteServerAkamaiCDN.rtt.domain.rest.SaveRTTRequest;
import agh.cs.RemoteServerAkamaiCDN.throughput.application.ThroughputService;
import agh.cs.RemoteServerAkamaiCDN.throughput.domain.ThroughputEntity;
import agh.cs.RemoteServerAkamaiCDN.throughput.domain.rest.SaveThroughputRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/throughput")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ThroughputController {
    private final ThroughputService service;

    @PostMapping("/save")
    public ResponseEntity<ThroughputEntity> save(@RequestBody SaveThroughputRequest request) {
        log.info("Packet Loss request");
        return ResponseEntity.ok(service.save(request));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ThroughputEntity>> getAll() {
        List<ThroughputEntity> entities = service.getAll();
        if (entities.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(entities);
    }

    @GetMapping
    public ResponseEntity<List<ThroughputEntity>> getAllBetweenDates(
            @RequestParam(name = "startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Date start,
            @RequestParam(name = "endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Date end) {
        if (start.after(end))
            return ResponseEntity.badRequest().build();

        List<ThroughputEntity> entities = service.getAllBetweenDates(start, end);
        if (entities.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(entities);
    }
}
