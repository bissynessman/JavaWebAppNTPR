package tvz.ntpr.core.service;

import tvz.ntpr.core.entity.Report;

public interface ReportService {
    void saveReport(Report report);
    String getStudentIdByReportId(String reportId);
}
