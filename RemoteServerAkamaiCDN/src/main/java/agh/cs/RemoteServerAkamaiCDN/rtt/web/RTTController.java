package agh.cs.RemoteServerAkamaiCDN.rtt.web;

import agh.cs.RemoteServerAkamaiCDN.packetLoss.domain.PacketLossEntity;
import agh.cs.RemoteServerAkamaiCDN.packetLoss.domain.PacketLossService;
import agh.cs.RemoteServerAkamaiCDN.packetLoss.domain.rest.SavePacketLossRequest;
import agh.cs.RemoteServerAkamaiCDN.rtt.domain.RTTEntity;
import agh.cs.RemoteServerAkamaiCDN.rtt.domain.RTTService;
import agh.cs.RemoteServerAkamaiCDN.rtt.domain.rest.SaveRTTRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Log4j2
@Validated
@RestController
@RequestMapping("/rtt")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RTTController {

    private final RTTService service;

    @PostMapping("/save")
    public ResponseEntity<List<RTTEntity>> save(@RequestBody SaveRTTRequest request) {
        log.info("Packet Loss request");
        return ResponseEntity.ok(service.save(request.getEntities()));
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
                    Date start,
            @RequestParam(name = "endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Date end) {
        if (start.after(end))
            return ResponseEntity.badRequest().build();

        List<RTTEntity> entities = service.getAllBetweenDates(start, end);
        if (entities.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(entities);
    }
}
