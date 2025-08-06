package tvz.ntpr.ntprdbrestapi.repo;

import org.apache.ibatis.annotations.Mapper;
import tvz.ntpr.ntprdbrestapi.entity.Student;

import java.util.List;

@Mapper
public interface StudentRepository extends BaseRepository<Student> {
    List<Student> findAll();
}
