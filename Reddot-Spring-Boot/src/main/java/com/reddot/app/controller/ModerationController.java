package com.reddot.app.controller;

import com.reddot.app.entity.ContentReport;
import com.reddot.app.service.moderation.ContentReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/moderation")
@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
public class ModerationController {

    private final ContentReportService contentReportService;

    public ModerationController(ContentReportService contentReportService) {
        this.contentReportService = contentReportService;
    }

    // Tạo báo cáo mới
    @PostMapping("/reports")
    public ResponseEntity<ContentReport> createReport(@RequestBody ContentReport report) {
        return ResponseEntity.ok(contentReportService.createReport(report));
    }

    // Lấy danh sách báo cáo
    @GetMapping("/reports")
    public ResponseEntity<List<ContentReport>> getReports() {
        return ResponseEntity.ok(contentReportService.getReports());
    }

    // Xử lý báo cáo
    @PutMapping("/reports/{id}/process")
    public ResponseEntity<String> processReport(@PathVariable("id") Integer reportId) {
        contentReportService.processReport(reportId);
        return ResponseEntity.ok("Report processed successfully");
    }
}
