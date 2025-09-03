package tvz.ntpr.api.repo;

import org.apache.ibatis.annotations.Mapper;
import tvz.ntpr.api.entity.Assignment;

import java.util.List;

@Mapper
public interface AssignmentRepository extends BaseRepository<Assignment> {
    List<Assignment> findAll();
    List<Assignment> findActiveForStudent(String studentId);
    List<Assignment> findAllForCourse(String courseId);
    List<Assignment> findAllForAssignment(String assignmentId);
}
