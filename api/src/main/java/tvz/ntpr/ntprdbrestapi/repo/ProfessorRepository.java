package tvz.ntpr.ntprdbrestapi.repo;

import org.apache.ibatis.annotations.Mapper;
import tvz.ntpr.ntprdbrestapi.entity.Professor;

import java.util.List;

@Mapper
public interface ProfessorRepository extends BaseRepository<Professor> {
    List<Professor> findAll();
    List<Professor> findAllUnauthorized();
    void authorize(String id);
}
