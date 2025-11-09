package com.livewatch.controller;

import com.livewatch.model.UserEvent;
import com.livewatch.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserActivityService userActivityService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserEvent event) {
        logger.info("Inside join controller - {}", event.toString());
        userActivityService.handleUserJoin(event);
        return ResponseEntity.ok("User joined");
    }

    @PostMapping("/leave")
    public ResponseEntity<String> leave(@RequestBody UserEvent event) {
        logger.info("Inside leave controller - {}", event.toString());
        userActivityService.handleUserLeave(event);
        return ResponseEntity.ok("User left");
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getLiveCount() throws Exception {
         Long count =  userActivityService.getLiveUserCount().get();

        return ResponseEntity.ok(count);
    }
}
