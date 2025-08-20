package com.backend.service;

import com.backend.model.Activity;
import com.backend.model.User;
import com.backend.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public void logActivity(User user, String activityType, String description, String ipAddress, String metadata) {
        Activity activity = new Activity();
        activity.setUser(user);
        activity.setActivityType(activityType);
        activity.setDescription(description);
        activity.setIpAddress(ipAddress);
        activity.setMetadata(metadata);
        activity.setCreatedAt(OffsetDateTime.now());
        activityRepository.save(activity);
    }

    public List<Activity> getActivitiesByUser(Long userId) {
        return activityRepository.findByUserId(userId);
    }
}
