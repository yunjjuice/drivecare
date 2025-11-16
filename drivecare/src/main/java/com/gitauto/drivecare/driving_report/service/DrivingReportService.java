package com.gitauto.drivecare.driving_report.service;

import com.gitauto.drivecare.database.user_driving_stat.entity.UserDrivingStatEntity;
import com.gitauto.drivecare.database.user_driving_stat.repository.UserDrivingStatRepository;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import com.gitauto.drivecare.database.user_info.repository.UserInfoRepository;
import com.gitauto.drivecare.driving_report.dto.UserDrivingStatReportDto;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DrivingReportService {

    private final UserInfoRepository userInfoRepository;
    private final UserDrivingStatRepository userDrivingStatRepository;
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    public void sendWeeklyReports() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.minusWeeks(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        int days = (int) ChronoUnit.DAYS.between(from, now); // 기간 계산
        String dateRange = formatDateRange(from, now);
        sendReportsForPeriod("주간", from, now, days, dateRange);
    }

    public void sendMonthlyReports() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        int days = (int) ChronoUnit.DAYS.between(from, now);
        String dateRange = formatDateRange(from, now);
        sendReportsForPeriod("월간", from, now, days, dateRange);
    }

    // 날짜 포맷 함수
    private String formatDateRange(LocalDateTime from, LocalDateTime to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        return from.format(formatter) + " ~ " + to.format(formatter);
    }

    private void sendReportsForPeriod(String period, LocalDateTime from, LocalDateTime to, int days, String dateRange) {
        List<UserInfoEntity> users = userInfoRepository.findAll();
        for (UserInfoEntity user : users) {
            List<UserDrivingStatEntity> stats = userDrivingStatRepository.findByUserInfoAndCreDtBetween(user, from, to);

            if (stats.isEmpty()) continue;

            Double driveScoreAvg = stats.stream().mapToDouble(e -> e.getDriveScore() == null ? 0.0 : e.getDriveScore()).average().orElse(0.0);

            int accelCountSum = stats.stream().mapToInt(e -> e.getAccelCount() == null ? 0 : e.getAccelCount()).sum();
            double accelCountAvg = days > 0 ? ((double) accelCountSum / days) : 0.0;

            int brakeCountSum = stats.stream().mapToInt(e -> e.getBrakeCount() == null ? 0 : e.getBrakeCount()).sum();
            double brakeCountAvg = days > 0 ? ((double) brakeCountSum / days) : 0.0;

            int handleMissCountSum = stats.stream().mapToInt(e -> e.getHandleMissCount() == null ? 0 : e.getHandleMissCount()).sum();
            double handleMissCountAvg = days > 0 ? ((double) handleMissCountSum / days) : 0.0;

            UserDrivingStatReportDto dto = UserDrivingStatReportDto.builder()
                    .userName(user.getName())
                    .userEmail(user.getEmail())
                    .driveScoreAvg(driveScoreAvg)
                    .accelCountSum(accelCountSum)
                    .accelCountAvg(accelCountAvg)
                    .brakeCountSum(brakeCountSum)
                    .brakeCountAvg(brakeCountAvg)
                    .handleMissCountSum(handleMissCountSum)
                    .handleMissCountAvg(handleMissCountAvg)
                    .period(period)
                    .days(days)
                    .dateRange(dateRange)
                    .build();

            sendReportMail(dto);
        }
    }

    private void sendReportMail(UserDrivingStatReportDto dto) {
        Context context = new Context();
        context.setVariable("userName", dto.getUserName());
        context.setVariable("driveScoreAvg", dto.getDriveScoreAvg());
        context.setVariable("accelCountSum", dto.getAccelCountSum());
        context.setVariable("accelCountAvg", dto.getAccelCountAvg());
        context.setVariable("brakeCountSum", dto.getBrakeCountSum());
        context.setVariable("brakeCountAvg", dto.getBrakeCountAvg());
        context.setVariable("handleMissCountSum", dto.getHandleMissCountSum());
        context.setVariable("handleMissCountAvg", dto.getHandleMissCountAvg());
        context.setVariable("period", dto.getPeriod());
        context.setVariable("days", dto.getDays());
        context.setVariable("dateRange", dto.getDateRange());
        context.setVariable("accelMax", 10.0);
        context.setVariable("brakeMax", 10.0);
        context.setVariable("handleMax", 10.0);

        String html = templateEngine.process("drivingReport.html", context);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(dto.getUserEmail());
            helper.setSubject("[DriveCare+] " + dto.getPeriod() + " 운전자 주행 리포트");
            helper.setText(html, true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}