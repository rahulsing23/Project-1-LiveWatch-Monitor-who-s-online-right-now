package com.livewatch.controller;

import com.livewatch.service.LiveWatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    private final LiveWatchService liveWatchService;

    public HomeController(LiveWatchService liveWatchService) {
        this.liveWatchService = liveWatchService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }


}


@RestController
class LiveWatchController{

    private final LiveWatchService liveWatchService;

    public LiveWatchController(LiveWatchService liveWatchService){
        this.liveWatchService = liveWatchService;
    }

    @GetMapping("/api/active-users/count")
    public ResponseEntity<Map<String, Object>> getActiveUserCount(){
        Map<String, Object> response = new HashMap<>();
        long count = liveWatchService.getActiveUserCount();
        response.put("count", count);
        response.put("capacityExceeded", liveWatchService.isServerCapacityExceeded());
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);

    }

}
