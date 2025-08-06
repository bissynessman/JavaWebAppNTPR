package tvz.ntpr.ntprdbrestapi.repo;

import tvz.ntpr.ntprdbrestapi.entity.BaseEntity;

import java.util.Optional;

public interface BaseRepository<T extends BaseEntity> {
    int create(T entity);
    Optional<T> findById(String id);
    int update(T entity);
    int deleteById(String id);
}
