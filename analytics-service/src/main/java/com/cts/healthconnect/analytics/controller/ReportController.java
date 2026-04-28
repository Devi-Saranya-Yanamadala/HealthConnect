package com.cts.healthconnect.analytics.controller;

import com.cts.healthconnect.analytics.dto.*;
import com.cts.healthconnect.analytics.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/analytics") 
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/reports")
    public ResponseEntity<List<ReportResponseDto>> listReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @PostMapping(value = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportResponseDto> generate(@RequestBody ReportRequestDto request) {
        return ResponseEntity.ok(reportService.generateReport(request));
    }

    @GetMapping("/download/{reportId}")
    public ResponseEntity<byte[]> download(@PathVariable String reportId) {
        byte[] pdfContent = reportService.generatePdfReport(reportId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("Hospital_Report_" + reportId + ".pdf")
                .build());

        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }
}