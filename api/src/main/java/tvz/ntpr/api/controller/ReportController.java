package tvz.ntpr.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tvz.ntpr.api.entity.Report;
import tvz.ntpr.api.service.ReportService;

import static tvz.ntpr.api.config.Urls.URL_ID;
import static tvz.ntpr.api.config.Urls.URL_REPORT;

@RestController
@RequestMapping(URL_REPORT)
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(URL_ID)
    public ResponseEntity<byte[]> downloadFile(@PathVariable String id) {
        Report file = reportService.findByStudent(id);
        if (file == null || file.getData() == null) {
            return  ResponseEntity.notFound().build();
        }

        String fileName = file.getFileName();
        byte[] fileData = file.getData();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName)
                .body(fileData);
    }

    @PostMapping
    public void saveReport(@RequestBody Report report) {
        reportService.create(report);
    }
}
