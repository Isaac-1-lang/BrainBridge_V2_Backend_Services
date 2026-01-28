package com.learn.brainbridge.serviceImpl;

import com.learn.brainbridge.service.AnalyticsService;
import com.learn.brainbridge.dtos.AnalyticsDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Override
    public AnalyticsDTO createAnalytics(AnalyticsDTO analyticsDTO) {
        // implementation
        return analyticsDTO;
    }

    @Override
    public AnalyticsDTO getAnalyticsById(Long id) {
        return null;
    }

    @Override
    public List<AnalyticsDTO> getAnalyticsByProjectId(Long projectId) {
        return List.of();
    }

    @Override
    public Map<String, Long> getAnalyticsSummary(Long projectId) {
        return Map.of();
    }

    @Override
    public void trackView(Long projectId, String ipAddress, String userAgent) {
    }

    @Override
    public void trackLike(Long projectId) {
    }

    @Override
    public void trackComment(Long projectId) {
    }
}
