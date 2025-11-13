package com.example.try_snap_bi.service.impl;

import com.example.try_snap_bi.service.SnapService;
import com.example.try_snap_bi.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
public class SnapServiceImpl implements SnapService {


    public String generateToken(String privateKey, String clientKey){
        try{
        int expireIn = 3600;
        long expireTime = new Date().getTime() + (expireIn * 1000);
        JwtUtil jwtUtil = new JwtUtil(privateKey);
        String token = jwtUtil.createTokenJWT(clientKey, "BCA", "token API", expireTime);
        return  token;
        }
        catch (Exception e){
            e.printStackTrace();
            return "gagall";
        }
    }

    public String generateSignature(String privateKey, String isoTime, String clientId){
        try{
            String message = clientId + "|" + isoTime;
            Signature signature = Signature.getInstance("SHA256withRSA");
            byte[] bytePrivateKey = Base64.getDecoder().decode(privateKey);
            PrivateKey privateKeyDecoded = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(bytePrivateKey));
            signature.initSign(privateKeyDecoded);
            signature.update(message.getBytes(StandardCharsets.UTF_8));

            byte[] signatureByte = signature.sign();

            return Base64.getEncoder().encodeToString(signatureByte);
        }
        catch (Exception e){
            e.printStackTrace();
            return "gagall";
        }
    }

}
