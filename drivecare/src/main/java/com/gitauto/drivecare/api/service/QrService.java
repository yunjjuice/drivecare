package com.gitauto.drivecare.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class QrService {
    public Resource getHomePageQr() throws IOException {
        Resource qr = new ClassPathResource("static/images/qr.png");

        return qr;
    }

}
