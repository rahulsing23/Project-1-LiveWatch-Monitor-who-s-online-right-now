package com.livewatch.service;

public interface LiveWatchService {

    long getActiveUserCount();

    long addUserAndGetCount(String sessionId);

    long removeUserAndGetCount(String sessionId);

    boolean isServerCapacityExceeded();
}
