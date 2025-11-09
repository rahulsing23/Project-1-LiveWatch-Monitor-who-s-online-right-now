package com.livewatch.controller;

import com.livewatch.model.UserEvent;
import com.livewatch.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserActivityService userActivityService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserEvent event) {
        userActivityService.handleUserJoin(event);
        return ResponseEntity.ok("User joined");
    }

    @PostMapping("/leave")
    public ResponseEntity<String> leave(@RequestBody UserEvent event) {
        userActivityService.handleUserLeave(event);
        return ResponseEntity.ok("User left");
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getLiveCount() throws Exception {
         Long count =  userActivityService.getLiveUserCount().get();

        return ResponseEntity.ok(count);
    }
}
