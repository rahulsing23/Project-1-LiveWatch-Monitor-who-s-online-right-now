package com.livewatch.model;


import java.time.LocalDateTime;


public class UserEvent {
    private String userId;
    private String sessionId;
    private String eventType; // JOIN or LEAVE
    private LocalDateTime eventTime;

    public UserEvent(String userId, String sessionId, String eventType) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.eventType = eventType;
        this.eventTime = LocalDateTime.now();
    }

    public UserEvent() {
    }

    public String getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getEventType() {
        return eventType;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public String toString() {
        return "UserEvent{" +
                "userId='" + userId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", eventTime=" + eventTime +
                '}';
    }
}
