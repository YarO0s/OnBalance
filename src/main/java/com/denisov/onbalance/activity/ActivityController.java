package com.denisov.onbalance.activity;

import com.denisov.onbalance.security.JWTService;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="activity", produces="application/json")
public class ActivityController {
    private final ActivityService activityService;
    private final ActivityValidationService activityValidationService;
    private final JWTService jwtService;

    public ActivityController(ActivityService activityService,
                              ActivityValidationService activityValidationService,
                              JWTService jwtService){
        this.activityService = activityService;
        this.activityValidationService =activityValidationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/new")
    public String saveActivity(@RequestHeader("Authorization") String authHeader, String title, String color,
                               String description){
        JSONObject result = new JSONObject();
        Long userId = jwtService.identifyUser(authHeader);
        return activityService.saveActivity(title, color, description, userId);
    }

    @PostMapping("/update/{id}")
    public String updateActivity(@RequestHeader("Authorization") String authHeader, @PathVariable(value="id") long id,
                                 String title, String color, String description, int completionStatus){
        Long userId = jwtService.identifyUser(authHeader);
        return activityService.updateActivity(id, title, color, description, completionStatus, userId);
    }

    //TODO: replace userId with userName
    @GetMapping("/find/{id}")
    public String getActivity(@RequestHeader("Authorization") String authHeader, @PathVariable(value = "id") long id){
        Long userId = jwtService.identifyUser(authHeader);
        return activityService.findActivityById(id, userId);
    }

    @GetMapping("/findAll")
    public String getAllActivities(@RequestHeader("Authorization") String authHeader){
        JSONObject result = new JSONObject();
        //TODO: catch header error validation
        Long userId = jwtService.identifyUser(authHeader);
        return activityService.findAllActivityByUserId(userId);
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable(value = "id") long id, @RequestHeader("Authorization") String authHeader){
        Long userId = jwtService.identifyUser(authHeader);
        return activityService.removeActivityById(id, userId);
    }

    @GetMapping("/ping")
    public String ping(@RequestHeader("Authorization") String authHeader){
        Long userId = jwtService.identifyUser(authHeader);
        return "pong " + userId;
    }

}
