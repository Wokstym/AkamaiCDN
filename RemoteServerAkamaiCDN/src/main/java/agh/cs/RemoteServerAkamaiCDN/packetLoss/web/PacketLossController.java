package agh.cs.RemoteServerAkamaiCDN.packetLoss.web;

import agh.cs.RemoteServerAkamaiCDN.packetLoss.domain.PacketLossEntity;
import agh.cs.RemoteServerAkamaiCDN.packetLoss.domain.PacketLossService;
import agh.cs.RemoteServerAkamaiCDN.packetLoss.domain.rest.SavePacketLossRequest;
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
@RequestMapping("/packet_loss")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PacketLossController {

    private final PacketLossService service;

    @PostMapping("/save")
    public ResponseEntity<List<PacketLossEntity>> save(@RequestBody SavePacketLossRequest request) {
        log.info("Packet Loss request");
        return ResponseEntity.ok(service.save(request.getEntities()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PacketLossEntity>> getAll() {
        List<PacketLossEntity> entities = service.getAll();
        if (entities.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(entities);
    }

    @GetMapping
    public ResponseEntity<List<PacketLossEntity>> getAllBetweenDates(
            @RequestParam(name = "startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Date start,
            @RequestParam(name = "endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    Date end) {
        if (start.after(end))
            return ResponseEntity.badRequest().build();

        List<PacketLossEntity> entities = service.getAllBetweenDates(start, end);
        if (entities.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(entities);
    }
}
