package com.learn.brainbridge.controllers;

import com.learn.brainbridge.entity.Favorite;
import com.learn.brainbridge.entity.User;
import com.learn.brainbridge.repository.UserRepository;
import com.learn.brainbridge.service.FavoriteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Favorites API", description = "Operations for user favorite projects")
@RequestMapping("/api/favorites")
public class FavoritesController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserRepository userRepository;

    private Long getUserIdFromAuth(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return null;
        String principalName = authentication.getName();
        User user = userRepository.findByEmail(principalName).orElseGet(() -> userRepository.findByUsername(principalName).orElse(null));
        return user != null ? user.getId() : null;
    }

    @GetMapping
    public ResponseEntity<?> getMyFavorites(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        
        List<Favorite> favorites = favoriteService.getUserFavorites(userId);
        List<Integer> projectIds = favorites.stream().map(Favorite::getProjectId).collect(Collectors.toList());
        return ResponseEntity.ok(projectIds);
    }

    @PostMapping("/{projectId}")
    public ResponseEntity<?> addFavorite(@PathVariable Integer projectId, Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        favoriteService.addFavorite(userId, projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Project added to favorites");
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> removeFavorite(@PathVariable Integer projectId, Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        favoriteService.removeFavorite(userId, projectId);
        return ResponseEntity.ok("Project removed from favorites");
    }

    @GetMapping("/{projectId}/check")
    public ResponseEntity<?> checkFavorite(@PathVariable Integer projectId, Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        boolean isFavorited = favoriteService.isFavorited(userId, projectId);
        return ResponseEntity.ok(isFavorited);
    }
}
