package com.data;

import com.report.AllureReport;
import com.report.ExtentReport;
import com.report.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportType {

    ALLURE(AllureReport.class),
    EXTENT(ExtentReport.class);

    private final Class<? extends Report> reportClass;

    /**
     * Gets the report type.
     * @param report report name
     * @return report type
     */
    public static ReportType getReport(String report) {
        if (report == null || report.isBlank()) {
            throw new IllegalArgumentException(
                    "Report cannot be null or empty" );
        } try {
            return valueOf(report.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException( "Unsupported report: " + report, e );
        }
    }
}
