package tvz.ntpr.api.repo;

import org.apache.ibatis.annotations.Mapper;
import tvz.ntpr.api.entity.Course;

import java.util.List;

@Mapper
public interface CourseRepository extends BaseRepository<Course> {
    List<Course> findAll();
    List<Course> findByProfessorId(String professorId);
}
