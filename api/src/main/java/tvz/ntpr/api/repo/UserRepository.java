package tvz.ntpr.api.repo;

import org.apache.ibatis.annotations.Mapper;
import tvz.ntpr.api.entity.User;

import java.util.List;

@Mapper
public interface UserRepository extends BaseRepository<User> {
    List<User> findAll();
    User findByUsername(String username);
    String findEmailByUserUuid(String userId);
}
