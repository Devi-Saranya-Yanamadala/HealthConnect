package com.cts.healthconnect.analytics.service;

import com.cts.healthconnect.analytics.client.PatientClient;
import com.cts.healthconnect.analytics.dto.*;
import com.cts.healthconnect.analytics.entity.*;
import com.cts.healthconnect.analytics.repository.ReportRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final PatientClient patientClient;

    @Override
    @Transactional
    public ReportResponseDto generateReport(ReportRequestDto request) {
        Report report = new Report();
        report.setReportName(request.getReportName());
        report.setReportType(request.getReportType());
        report.setCreatedAt(LocalDateTime.now());
        report.setGeneratedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        long count = 0;
        try {
            count = patientClient.getTotalPatients();
            report.setStatus("COMPLETED");
        } catch (Exception e) {
            report.setStatus("FAILED");
        }

        ReportContent content = new ReportContent();
        content.setReport(report);
        content.setHospitalDataJson("{\"totalPatients\": " + count + "}");
        report.setContent(content);

        return mapToDto(reportRepository.save(report));
    }

    @Override
    public List<ReportResponseDto> getAllReports() {
        return reportRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public byte[] generatePdfReport(String reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph("Hospital Report: " + report.getReportName()));
            document.add(new Paragraph("Data Details: " + report.getContent().getHospitalDataJson()));
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("PDF Generation Error", e);
        }
    }

    private ReportResponseDto mapToDto(Report r) {
        return new ReportResponseDto(r.getReportId(), r.getReportName(), r.getStatus(), r.getCreatedAt(), r.getGeneratedBy());
    }
}