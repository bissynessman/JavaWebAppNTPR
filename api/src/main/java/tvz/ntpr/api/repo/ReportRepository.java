package tvz.ntpr.api.repo;

import org.apache.ibatis.annotations.Mapper;
import tvz.ntpr.api.entity.Report;

import java.util.Optional;

@Mapper
public interface ReportRepository extends BaseRepository<Report> {
    Optional<Report> findByStudentId(String studentId);
    int deleteByStudentId(String studentId);
}
