package com.example.try_snap_bi.controller;

import com.example.try_snap_bi.dto.ResponseDto;
import com.example.try_snap_bi.service.impl.SnapServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/snap")
public class SnapController {

    @Value("${config.private-key}")
    private String privateKey;

    @Autowired
    private SnapServiceImpl snapService;

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("test");
    }

    @PostMapping("/generate-time")
    public ResponseEntity<String> generateTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        String timestamp = ZonedDateTime.now(ZoneOffset.of("+07:00")).format(formatter);

        return ResponseEntity.ok(timestamp);
    }

    @PostMapping("/generate-signature")
    public ResponseEntity<String> generateSignature(@RequestHeader(value = "Timestamp", required = true) String isoTime,
                                                    @RequestHeader(value = "Client-Key", required = true) String clientId){
        String response = snapService.generateSignature(privateKey, isoTime, clientId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate-token")
    public ResponseEntity<?> generateToken(@RequestHeader(value = "Client-Key", required = true) String clientKey,
                                                @RequestBody(required = false) ResponseDto requestBody){
        String response = snapService.generateToken(privateKey, clientKey);
        System.out.println(requestBody + " <-- requestBody");
        if (requestBody != null){
            return ResponseEntity.ok(requestBody);
        }
        return ResponseEntity.ok(response);
    }
}
