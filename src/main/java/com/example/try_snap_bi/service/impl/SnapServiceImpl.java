package com.example.try_snap_bi.service.impl;

import com.example.try_snap_bi.service.SnapService;
import com.example.try_snap_bi.util.JwtUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
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

    @Override
    public String generateSignatureInbound(String clientSecret, String httpMethod, String relativeUrl, String token, String timeStamp, String body) {
        try{
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(body);
            body = gson.toJson(jsonElement);
            String hexEncode = hash256(body);
            String message = httpMethod + ":" + relativeUrl + ":" + token + ":" + hexEncode + ":" + timeStamp;
            System.out.println("ini message ->" + message);

            String response = Base64.getEncoder().encodeToString(calculateHMACSHA512(message, clientSecret));
            return response;

        }
        catch (Exception e){
            e.printStackTrace();
            return "gagall";
        }
    }

    @Override
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
    private String hash256(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        String hexStr = "";
        for (int i = 0; i < hash.length; i++) {
            hexStr += Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1);
        }
        return hexStr.toLowerCase().replace("-", "");
    }

    public byte[] calculateHMACSHA512(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(secretKeySpec);
        return mac.doFinal(data.getBytes());
    }

}
