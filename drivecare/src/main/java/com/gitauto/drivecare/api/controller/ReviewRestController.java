package com.gitauto.drivecare.api.controller;

import com.gitauto.drivecare.api.dto.ApiResponse;
import com.gitauto.drivecare.api.dto.ReviewRequestDto;
import com.gitauto.drivecare.api.service.ReviewService;
import com.gitauto.drivecare.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewRestController {

    private final ReviewService reviewService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<?>> submitReview(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @RequestBody ReviewRequestDto request) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtTokenProvider.getSubject(token);

        return ResponseEntity.ok(ApiResponse.ok(reviewService.saveReviewRating(userId, request)));
    }
}
