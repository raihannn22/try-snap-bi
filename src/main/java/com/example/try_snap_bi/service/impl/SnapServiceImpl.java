package com.example.try_snap_bi.service.impl;

import com.example.try_snap_bi.service.SnapService;
import com.example.try_snap_bi.util.JwtUtil;
import org.springframework.stereotype.Service;

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
}
