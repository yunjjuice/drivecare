package com.gitauto.drivecare.driving_report.scheduler;

import com.gitauto.drivecare.driving_report.service.DrivingReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DrivingReportScheduler {

    private final DrivingReportService drivingReportService;

    // 테스트
//    @Scheduled(cron = "0 * * * * *")
//    public void sendWeeklyReportTest() {
//        System.out.println("Triggered Scheduler!");
//        drivingReportService.sendWeeklyReports();
//    }

    // 매주 월요일 오전 9시에 발송
    @Scheduled(cron = "0 0 9 * * MON")
    public void sendWeeklyReport() {
        drivingReportService.sendWeeklyReports();
    }

    // 매월 1일 오전 9시에 발송
    @Scheduled(cron = "0 0 9 1 * *")
    public void sendMonthlyReport() {
        drivingReportService.sendMonthlyReports();
    }
}