package tvz.ntpr.ntprdbrestapi.repo;

import org.apache.ibatis.annotations.Mapper;
import tvz.ntpr.ntprdbrestapi.entity.User;

import java.util.List;

@Mapper
public interface UserRepository extends BaseRepository<User> {
    List<User> findAll();
    User findByUsername(String username);
}
