package com.livewatch.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketBroadcastService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketBroadcastService(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void broadcastLiveCount(Long liveCount){
        simpMessagingTemplate.convertAndSend("/topic/liveCount", liveCount);
    }
}
