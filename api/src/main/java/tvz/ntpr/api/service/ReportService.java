package tvz.ntpr.api.service;

import org.springframework.stereotype.Service;
import tvz.ntpr.api.entity.Report;
import tvz.ntpr.api.repo.ReportRepository;

@Service
public class ReportService extends BaseService<Report> {
    private final ReportRepository repository;

    public ReportService(ReportRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Report create(Report entity) {
        deleteByStudent(entity.getStudent());
        return super.create(entity);
    }

    public Report findByStudent(String studentId) {
        return repository.findByStudentId(studentId).orElse(null);
    }

    private void deleteByStudent(String studentId) {
        repository.deleteByStudentId(studentId);
    }
}
