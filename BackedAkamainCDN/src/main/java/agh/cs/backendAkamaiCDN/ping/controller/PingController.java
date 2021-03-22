package agh.cs.backendAkamaiCDN.ping.controller;

import agh.cs.backendAkamaiCDN.ping.service.PingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PingController {

    private final PingService service;

    @GetMapping
    public ResponseEntity<String> ping() {
        String response = service.savePing();
        return ResponseEntity.ok(response);
    }

}
