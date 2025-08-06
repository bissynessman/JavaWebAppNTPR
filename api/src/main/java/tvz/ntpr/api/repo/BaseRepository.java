package tvz.ntpr.api.repo;

import tvz.ntpr.api.entity.BaseEntity;

import java.util.Optional;

public interface BaseRepository<T extends BaseEntity> {
    int create(T entity);
    Optional<T> findById(String id);
    int update(T entity);
    int deleteById(String id);
}
