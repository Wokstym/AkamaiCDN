package agh.cs.backendAkamaiCDN.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/ping")
public class PingController {

    @GetMapping
    public ResponseEntity<String> addIngredient() {
        Date date = new Date(System.currentTimeMillis());
        String result = String.format("Server time: %s", date);
        return ResponseEntity.ok(result);
    }

}
