package com.kosa.mini.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.kosa.mini.api.entity.RefreshTokenRedis;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshTokenRedis, Integer> {
    void deleteById(Integer memberId);
}
