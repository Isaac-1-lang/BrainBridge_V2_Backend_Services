package com.learn.brainbridge.service;

import com.learn.brainbridge.entity.Favorite;
import com.learn.brainbridge.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    public List<Favorite> getUserFavorites(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    public boolean isFavorited(Long userId, Integer projectId) {
        return favoriteRepository.existsByUserIdAndProjectId(userId, projectId);
    }

    @Transactional
    public Favorite addFavorite(Long userId, Integer projectId) {
        if (!favoriteRepository.existsByUserIdAndProjectId(userId, projectId)) {
            Favorite favorite = new Favorite(userId, projectId);
            return favoriteRepository.save(favorite);
        }
        return favoriteRepository.findByUserIdAndProjectId(userId, projectId).get();
    }

    @Transactional
    public void removeFavorite(Long userId, Integer projectId) {
        favoriteRepository.deleteByUserIdAndProjectId(userId, projectId);
    }
}
