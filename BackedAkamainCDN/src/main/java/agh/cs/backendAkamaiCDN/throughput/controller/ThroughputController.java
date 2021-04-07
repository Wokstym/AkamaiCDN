package agh.cs.backendAkamaiCDN.throughput.controller;

import agh.cs.backendAkamaiCDN.throughput.entity.ThroughputEntity;
import agh.cs.backendAkamaiCDN.throughput.service.ThroughputService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/throughput")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ThroughputController {
    private final ThroughputService service;
    @GetMapping
    public List<ThroughputEntity> getAll(){
        return service.getAll();
    }

}
