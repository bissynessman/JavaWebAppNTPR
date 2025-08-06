package tvz.ntpr.ntprdbrestapi.repo;

import org.apache.ibatis.annotations.Mapper;
import tvz.ntpr.ntprdbrestapi.auth.JwToken;

@Mapper
public interface RefreshTokenRepository extends BaseRepository<JwToken>{
    JwToken findByToken(String token);
    int deleteByUsername(String username);
}
