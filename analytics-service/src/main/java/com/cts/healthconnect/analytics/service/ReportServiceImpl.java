package com.cts.healthconnect.analytics.service;

import com.cts.healthconnect.analytics.client.*;
import com.cts.healthconnect.analytics.dto.*;
import com.cts.healthconnect.analytics.entity.*;
import com.cts.healthconnect.analytics.repository.ReportRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository  reportRepository;
    private final PatientClient     patientClient;
    private final AppointmentClient appointmentClient;
    private final BillingClient     billingClient;
    private final WardClient        wardClient;

    @Override
    @Transactional
    public ReportResponseDto generateReport(ReportRequestDto request) {

        Report report = new Report();
        report.setReportName(request.getReportName());
        report.setReportType(request.getReportType());
        report.setCreatedAt(LocalDateTime.now());
        report.setGeneratedBy(
            SecurityContextHolder.getContext().getAuthentication().getName()
        );

        // ✅ Fetch ONLY the data relevant to the selected report type
        String dataJson = buildDataJson(request.getReportType());
        report.setStatus("COMPLETED");

        ReportContent content = new ReportContent();
        content.setReport(report);
        content.setHospitalDataJson(dataJson);
        report.setContent(content);

        return mapToDto(reportRepository.save(report));
    }

    /* ─── BUILD JSON BASED ON REPORT TYPE ─── */
    private String buildDataJson(String reportType) {
        if (reportType == null) return "{}";

        switch (reportType) {

            case "PATIENT_SUMMARY": {
                long total = fetchSafe(() -> patientClient.getTotalPatients(), 0L);
                return String.format(
                    "{\"totalPatients\": %d}", total
                );
            }

            case "APPOINTMENT_ANALYTICS": {
                long total = fetchSafe(() -> appointmentClient.getTotalAppointments(), 0L);
                return String.format(
                    "{\"totalAppointments\": %d}", total
                );
            }

            case "BILLING_SUMMARY": {
                double revenue = fetchSafe(() -> billingClient.getTotalRevenue(), 0.0);
                return String.format(
                    "{\"totalRevenue\": %.2f}", revenue
                );
            }

            case "WARD_UTILIZATION": {
                long total  = fetchSafe(() -> wardClient.getTotalAdmissions(), 0L);
                long active = fetchSafe(() -> wardClient.getActiveAdmissions(), 0L);
                return String.format(
                    "{\"totalAdmissions\": %d, \"activeAdmissions\": %d}",
                    total, active
                );
            }

            case "DOCTOR_PERFORMANCE": {
                // Fetch all available metrics as a combined overview
                long   patients     = fetchSafe(() -> patientClient.getTotalPatients(), 0L);
                long   appointments = fetchSafe(() -> appointmentClient.getTotalAppointments(), 0L);
                return String.format(
                    "{\"totalPatients\": %d, \"totalAppointments\": %d}",
                    patients, appointments
                );
            }

            default:
                return "{}";
        }
    }

    /* ─── SAFE FEIGN CALL WRAPPER ─── */
    @FunctionalInterface
    interface FeignCall<T> { T call() throws Exception; }

    private <T> T fetchSafe(FeignCall<T> call, T fallback) {
        try {
            T result = call.call();
            System.out.println(">>> REPORT FETCH OK: " + result);
            return result;
        } catch (Exception e) {
            System.err.println(">>> REPORT FETCH FAILED: " + e.getMessage());
            return fallback;
        }
    }

    @Override
    public List<ReportResponseDto> getAllReports() {
        return reportRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] generatePdfReport(String reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found: " + reportId));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

            Font titleFont   = new Font(Font.HELVETICA, 18, Font.BOLD,   Color.decode("#1E293B"));
            Font labelFont   = new Font(Font.HELVETICA, 11, Font.BOLD,   Color.decode("#64748B"));
            Font valueFont   = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.decode("#1E293B"));
            Font sectionFont = new Font(Font.HELVETICA, 13, Font.BOLD,   Color.decode("#0F172A"));
            Font footerFont  = new Font(Font.HELVETICA,  9, Font.ITALIC, Color.GRAY);

            // ── Title ──
            Paragraph title = new Paragraph(
                "HealthConnect — " + report.getReportName(), titleFont
            );
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // ── Metadata ──
            document.add(metaRow("Report Type   :", report.getReportType(),  labelFont, valueFont));
            document.add(metaRow("Generated By  :", report.getGeneratedBy(), labelFont, valueFont));
            document.add(metaRow("Generated At  :",
                report.getCreatedAt() != null ? report.getCreatedAt().format(fmt) : "-",
                labelFont, valueFont));
            document.add(metaRow("Status        :", report.getStatus(), labelFont, valueFont));

            document.add(new Paragraph("________________________________________________"));
            document.add(new Paragraph(" "));

            // ── Metrics section heading ──
            Paragraph metricsTitle = new Paragraph(
                getSectionTitle(report.getReportType()), sectionFont
            );
            metricsTitle.setSpacingBefore(10);
            metricsTitle.setSpacingAfter(10);
            document.add(metricsTitle);

            // ── Only the relevant metrics for this report type ──
            String json = report.getContent() != null
                ? report.getContent().getHospitalDataJson()
                : "{}";

            Map<String, String> metrics = getMetricLabels(report.getReportType());
            for (Map.Entry<String, String> entry : metrics.entrySet()) {
                String raw   = extractJsonValue(json, entry.getKey());
                String value = formatValue(entry.getKey(), raw);
                document.add(metaRow(entry.getValue() + " :", value, labelFont, valueFont));
            }

            // ── Footer ──
            document.add(new Paragraph(" "));
            document.add(new Paragraph("________________________________________________"));
            Paragraph footer = new Paragraph(
                "Auto-generated by HealthConnect on " +
                LocalDateTime.now().format(fmt), footerFont
            );
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("PDF Generation Error: " + e.getMessage(), e);
        }
    }

    /* ─── SECTION TITLE PER REPORT TYPE ─── */
    private String getSectionTitle(String reportType) {
        if (reportType == null) return "Report Data";
        switch (reportType) {
            case "PATIENT_SUMMARY":       return "Patient Summary";
            case "APPOINTMENT_ANALYTICS": return "Appointment Analytics";
            case "BILLING_SUMMARY":       return "Billing Summary";
            case "WARD_UTILIZATION":      return "Ward Utilization";
            case "DOCTOR_PERFORMANCE":    return "Doctor Performance Overview";
            default:                      return "Report Data";
        }
    }

    /* ─── METRIC KEYS + LABELS PER REPORT TYPE ─── */
    // LinkedHashMap preserves insertion order in the PDF
    private Map<String, String> getMetricLabels(String reportType) {
        Map<String, String> map = new LinkedHashMap<>();
        if (reportType == null) return map;

        switch (reportType) {
            case "PATIENT_SUMMARY":
                map.put("totalPatients", "Total Patients");
                break;

            case "APPOINTMENT_ANALYTICS":
                map.put("totalAppointments", "Total Appointments");
                break;

            case "BILLING_SUMMARY":
                map.put("totalRevenue", "Total Revenue (Rs.)");
                break;

            case "WARD_UTILIZATION":
                map.put("totalAdmissions",  "Total Admissions");
                map.put("activeAdmissions", "Active Admissions");
                break;

            case "DOCTOR_PERFORMANCE":
                map.put("totalPatients",     "Total Patients Served");
                map.put("totalAppointments", "Total Appointments");
                break;
        }
        return map;
    }

    /* ─── FORMAT VALUE BY KEY ─── */
    private String formatValue(String key, String raw) {
        if (key.equals("totalRevenue")) {
            return "Rs. " + raw;
        }
        return raw;
    }

    /* ─── HELPERS ─── */
    private Paragraph metaRow(String label, String value,
                               Font labelFont, Font valueFont) {
        Paragraph p = new Paragraph();
        p.add(new Chunk(label + "  ", labelFont));
        p.add(new Chunk(value != null ? value : "-", valueFont));
        p.setSpacingAfter(6);
        return p;
    }

    private String extractJsonValue(String json, String key) {
        try {
            String search = "\"" + key + "\":";
            int idx = json.indexOf(search);
            if (idx == -1) return "0";
            int start = idx + search.length();
            while (start < json.length() && json.charAt(start) == ' ') start++;
            int end = start;
            while (end < json.length()
                   && json.charAt(end) != ','
                   && json.charAt(end) != '}') end++;
            return json.substring(start, end).trim();
        } catch (Exception e) {
            return "0";
        }
    }

    private ReportResponseDto mapToDto(Report r) {
        return new ReportResponseDto(
            r.getReportId(),
            r.getReportName(),
            r.getStatus(),
            r.getCreatedAt(),
            r.getGeneratedBy()
        );
    }
}