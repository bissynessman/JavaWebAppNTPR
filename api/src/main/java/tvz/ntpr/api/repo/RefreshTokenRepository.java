package tvz.ntpr.api.repo;

import org.apache.ibatis.annotations.Mapper;
import tvz.ntpr.api.auth.JwToken;

@Mapper
public interface RefreshTokenRepository extends BaseRepository<JwToken>{
    JwToken findByToken(String token);
    int deleteByUsername(String username);
}
