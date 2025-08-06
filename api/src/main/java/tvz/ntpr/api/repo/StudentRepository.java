package tvz.ntpr.api.repo;

import org.apache.ibatis.annotations.Mapper;
import tvz.ntpr.api.entity.Student;

import java.util.List;

@Mapper
public interface StudentRepository extends BaseRepository<Student> {
    List<Student> findAll();
}
