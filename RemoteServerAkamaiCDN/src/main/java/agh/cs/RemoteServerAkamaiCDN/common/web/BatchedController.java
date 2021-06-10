package agh.cs.RemoteServerAkamaiCDN.common.web;

import agh.cs.RemoteServerAkamaiCDN.packetLoss.domain.PacketLossService;
import agh.cs.RemoteServerAkamaiCDN.rtt.domain.RTTService;
import agh.cs.RemoteServerAkamaiCDN.throughput.domain.ThroughputService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Validated
@RestController
@RequestMapping("/batched")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BatchedController {

    private final PacketLossService packetLossService;
    private final RTTService rttService;
    private final ThroughputService throughputService;

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody BatchedRequest request) {
        log.info("Packet Loss request");

        packetLossService.save(request.getPacketLossDtos());
        rttService.save(request.getRttDtos());
        throughputService.save(request.getThroughputRequests());

        return ResponseEntity.ok().build();
    }
}
