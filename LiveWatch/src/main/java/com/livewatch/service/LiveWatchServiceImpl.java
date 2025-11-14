package com.livewatch.service;

import com.livewatch.config.ServerConfig;
import com.livewatch.exception.InvalidSessionIdException;
import com.livewatch.exception.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class LiveWatchServiceImpl implements LiveWatchService{

    private final RedisTemplate<String, String> redisTemplate;
    private final ServerConfig serverConfig;

    private static final String ACTIVE_USER_KEY = "activeUsers";

    public LiveWatchServiceImpl(RedisTemplate<String, String> redisTemplate, ServerConfig serverConfig) {
        this.redisTemplate = redisTemplate;
        this.serverConfig = serverConfig;
    }

    @Override
    public long getActiveUserCount() {
        String operationId = UUID.randomUUID().toString();
        MDC.put("operationId", operationId);
        MDC.put("operation", "getActiveUserCount");

        try {
            Long size = redisTemplate.opsForSet().size(ACTIVE_USER_KEY);
            long userCount = size !=null ? size : 0L;

            MDC.put("userCount", String.valueOf(userCount));
            MDC.put("capacityUtilization", String.valueOf((double) userCount / serverConfig.getMaxUsers() * 100));

            if(userCount >= serverConfig.getWarningThreshold()){
                log.warn("Approaching server capacity: usercount={}, threshold={}, utilization={}%",
                        userCount, serverConfig.getWarningThreshold(), (double) (userCount/serverConfig.getMaxUsers())*100);

            }
            else{
                log.debug("Retrieved active user count: userCount={}", userCount);
            }

            return userCount;

        } catch (Exception e) {
            MDC.put("error", e.getClass().getSimpleName());
            log.error("Failed to get active user count from Redis: error={}, message={}",
                    e.getClass().getSimpleName(), e.getMessage());

            return 0L;
        }
        finally {
            MDC.clear();
        }


    }

    @Override
    public long addUserAndGetCount(String sessionId) {
        validateSessionId(sessionId);

        long currentCount = getActiveUserCount();

        if (currentCount >= serverConfig.getMaxUsers()) {
            log.error("Server capacity exceeded! Cannot add user {}. Current: {}, Max: {}",
                    sessionId, currentCount, serverConfig.getMaxUsers());
            throw new ServiceUnavailableException("Server is at maximum capacity");
        }

        try {
            return redisTemplate.execute(new SessionCallback<Long>() {
                @Override
                @SuppressWarnings("unchecked")
                public Long execute(RedisOperations operations) throws DataAccessException {
                    operations.multi();
                    operations.opsForSet().add(ACTIVE_USER_KEY, sessionId);
                    operations.opsForSet().size(ACTIVE_USER_KEY);

                    List<Object> results = operations.exec();

                    if(null == results || results.size() < 2){
                        throw new ServiceUnavailableException("Redis transaction failed", null);
                    }

                    Long newCount = (Long) results.get(1);
                    log.debug("Atomically added user: {}. New count: {}", sessionId, newCount);
                    return newCount;

                }
            });
        } catch (Exception e) {
            log.error("Failed to atomically add user {} to Redis: {}", sessionId, e.getMessage());
            throw new ServiceUnavailableException("Failed to add user", e);
        }


    }

    @Override
    public long removeUserAndGetCount(String sessionId) {
        validateSessionId(sessionId);

        try {

            return redisTemplate.execute(new SessionCallback<Long>() {
                @Override
                @SuppressWarnings("unchecked")
                public  Long execute(RedisOperations operations) throws DataAccessException {
                    operations.multi();
                    operations.opsForSet().remove(ACTIVE_USER_KEY, sessionId);
                    operations.opsForSet().size(ACTIVE_USER_KEY);
                    List<Object> results = operations.exec();


                    if (results == null || results.size() < 2) {
                        throw new ServiceUnavailableException("Redis transaction failed", null);
                    }

                    Long newCount = (Long) results.get(1);
                    log.debug("Atomically removed user: {}. New count: {}", sessionId, newCount);
                    return newCount;
                }
            });

        } catch (Exception e) {
            log.error("Failed to atomically remove user {} from Redis: {}", sessionId, e.getMessage());
            throw new ServiceUnavailableException("Failed to remove user", e);
        }
    }

    @Override
    public boolean isServerCapacityExceeded() {
        return getActiveUserCount() >= serverConfig.getMaxUsers();
    }

    private void validateSessionId(String sessionId){
        if(null == sessionId){
            throw new InvalidSessionIdException("Session ID cannot be null");
        }
        if(sessionId.trim().isEmpty()){
            throw new InvalidSessionIdException("Session ID cannot be empty");
        }
        if (sessionId.length() > 100) {
            throw new InvalidSessionIdException("Session ID too long (max 100 characters)");
        }
        if (!sessionId.matches("^[a-zA-Z0-9_-]+$")) {
            throw new InvalidSessionIdException("Session ID contains invalid characters");
        }
    }
}
