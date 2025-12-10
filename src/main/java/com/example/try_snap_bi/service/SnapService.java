package com.example.try_snap_bi.service;

import com.example.try_snap_bi.dto.AccessToken;

public interface SnapService {

    String generateToken(String privateKey, String clientKey);
    String generateSignatureInbound(String clientSecret,String httpMethod, String relativeUrl, String token,
                                String timeStamp, String body);
    String generateSignature(String privateKey, String isoTime, String clientId);
    AccessToken generateToken2(String privateKey, String clientKey);
}
