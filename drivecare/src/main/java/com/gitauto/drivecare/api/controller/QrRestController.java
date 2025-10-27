package com.gitauto.drivecare.api.controller;

import com.gitauto.drivecare.api.service.QrService;
import com.gitauto.drivecare.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qr")
public class QrRestController {

    private final QrService qrService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/homepage")
    public ResponseEntity<Resource> getQrHomepage(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) throws IOException {
        String token = authHeader.replace("Bearer ", "");
        jwtTokenProvider.getSubject(token);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"qr.png\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(qrService.getHomePageQr());
    }
}
