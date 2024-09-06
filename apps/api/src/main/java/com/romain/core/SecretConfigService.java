package com.romain.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SecretConfigService {

    @Value("${secret.key}")
    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }
}