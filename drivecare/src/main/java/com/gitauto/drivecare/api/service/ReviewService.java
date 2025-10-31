package com.gitauto.drivecare.api.service;

import com.gitauto.drivecare.api.dto.ReviewRequestDto;
import com.gitauto.drivecare.database.repair_reservation.entity.RepairReservationEntity;
import com.gitauto.drivecare.database.repair_reservation.repository.RepairReservationRepository;
import com.gitauto.drivecare.database.repair_review.entity.RepairReviewEntity;
import com.gitauto.drivecare.database.repair_review.repository.RepairReviewRepository;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import com.gitauto.drivecare.database.user_info.repository.UserInfoRepository;
import com.gitauto.drivecare.exception.ApiException;
import com.gitauto.drivecare.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final UserInfoRepository userInfoRepository;
    private final RepairReviewRepository repairReviewRepository;
    private final RepairReservationRepository repairReservationRepository;

    @Transactional
    public String saveReviewRating(String userId, ReviewRequestDto repairReview) {
        try {
            UserInfoEntity user = userInfoRepository.findByUserId(userId)
                    .orElseThrow(() -> new ApiException(ErrorCode.INVALIE_USERID, "User not found with userId: " + userId));

            RepairReservationEntity reservation = repairReservationRepository.findTop1ByIdOrderByCreDtDesc((repairReview.getReservationId()))
                    .orElseThrow(() -> new ApiException(ErrorCode.INVALID_RESERVATION_ID, "Reservation not found with id: " + repairReview.getReservationId()));

            Optional<RepairReviewEntity> optionalReview = repairReviewRepository.findByUserInfo_IdAndRepairReservation_Id(user.getId(), reservation.getId());

            RepairReviewEntity review;
            if (optionalReview.isPresent()) {
                review = optionalReview.get();
                review.setRating(repairReview.getRating());
                review.setReviewText(repairReview.getReviewText());
            } else {
                review = RepairReviewEntity.builder()
                        .userInfo(user)
                        .repairReservation(reservation)
                        .rating(repairReview.getRating())
                        .reviewText(repairReview.getReviewText())
                        .build();
            }

            repairReviewRepository.save(review);

            return "데이터 저장 완료";
        } catch (Exception e) {
            return "데이터 저장 실패: " + e.getMessage();
        }
    }
}
