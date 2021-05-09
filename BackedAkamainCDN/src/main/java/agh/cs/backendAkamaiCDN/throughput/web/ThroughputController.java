package agh.cs.backendAkamaiCDN.throughput.web;

import agh.cs.backendAkamaiCDN.throughput.application.ThroughputService;
import agh.cs.backendAkamaiCDN.throughput.domain.ThroughputEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/throughput")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ThroughputController {
    private final ThroughputService service;

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
