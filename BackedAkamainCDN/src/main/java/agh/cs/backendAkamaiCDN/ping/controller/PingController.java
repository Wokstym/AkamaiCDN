package agh.cs.backendAkamaiCDN.ping.controller;

import agh.cs.backendAkamaiCDN.ping.entity.PingEntity;
import agh.cs.backendAkamaiCDN.ping.service.PingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ping")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PingController {

    private final PingService service;

    @GetMapping("/{siteName}")
    public ResponseEntity<String> pingSite(@PathVariable String siteName) {
        String response = service.savePing(siteName);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<PingEntity> getAll(){
        return service.getAll();
    }

}
