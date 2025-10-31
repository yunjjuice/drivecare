package com.gitauto.drivecare.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewRequestDto {
    private Long reservationId;
    private int rating;
    private String reviewText;
}
