package com.learn.brainbridge.repository;

import com.learn.brainbridge.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    Optional<Favorite> findByUserIdAndProjectId(Long userId, Integer projectId);
    boolean existsByUserIdAndProjectId(Long userId, Integer projectId);
    void deleteByUserIdAndProjectId(Long userId, Integer projectId);
}
