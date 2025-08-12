package tvz.ntpr.core.service;

import tvz.ntpr.core.helper.ReportWrapper;

public interface ReportService {
    void saveReport(ReportWrapper reportWrapper);
    String getStudentIdByReportId(String reportId);
}
