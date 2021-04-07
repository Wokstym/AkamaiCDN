package agh.cs.backendAkamaiCDN.throughput.controller;

import agh.cs.backendAkamaiCDN.throughput.entity.ThroughputEntity;
import agh.cs.backendAkamaiCDN.throughput.service.ThroughputService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/throughput")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ThroughputController {
    private final ThroughputService service;
    @GetMapping("/all")
    public List<ThroughputEntity> getAll(){
        return service.getAll();
    }

    @GetMapping
    public List<ThroughputEntity> getAllBetweenDates(
            @RequestParam(name = "start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
            @RequestParam(name = "end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end
    ){
        return service.getAllBetweenDates(start, end);
    }
}
