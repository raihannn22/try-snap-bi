package com.example.try_snap_bi.controller;

import com.example.try_snap_bi.dto.ResponseDto;
import com.example.try_snap_bi.service.impl.SnapServiceImpl;
import org.apache.coyote.Response;
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

    @PostMapping("/test/signature-inbound")
    public ResponseEntity<?> testSignatureInbound(@RequestBody ResponseDto body){
        if(body == null) {
            return ResponseEntity.ok("oke");
        }
        if (body.getResponseCode() == null && body.getResponseMessage() == null) {
            return ResponseEntity.ok("oke");
        }
        return ResponseEntity.ok(body);
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

    @PostMapping("/generate-signature-inbound")
    public ResponseEntity<String> generateSignatureInbound(@RequestHeader(value = "Timestamp") String isoTime,
                                                           @RequestHeader(value = "Token") String token,
                                                           @RequestHeader(value ="Client-Secret") String clientSecret,
                                                           @RequestHeader(value = "HTTP-Method") String httpMethod,
                                                           @RequestHeader(value = "Relative-URL") String relativeUrl,
                                                           @RequestBody String body
                                                           ) {
        String response = snapService.generateSignatureInbound(clientSecret,httpMethod,relativeUrl,token,isoTime,body);
        return ResponseEntity.ok(response);
    //signatureUtil.validateServiceSignature(clientSecret,httpMethod,relativeUrl,token,timeStamp,body)
    }

    @PostMapping("/generate-token")
    public ResponseEntity<?> generateToken(@RequestHeader(value = "Client-Key", required = true) String clientKey,
                                                @RequestBody(required = false) ResponseDto requestBody){
        String response = snapService.generateToken(privateKey, clientKey);
        System.out.println(requestBody + " <-- requestBody");
        if(requestBody == null) {
            return ResponseEntity.ok(response);
        }

        if (requestBody.getResponseCode() == null && requestBody.getResponseMessage() == null) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(requestBody);
    }
}
