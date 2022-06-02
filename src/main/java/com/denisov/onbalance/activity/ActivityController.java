package com.denisov.onbalance.activity;

import com.denisov.onbalance.security.JWTService;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(name="activity", produces="application/json")
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
        activityService.saveActivity(title, color, description, userId);
        return result.put("result: ", "successful: ").toString();
    }

    //TODO: replace userId with userName
    @GetMapping("/{id}/")
    public String getActivity(@PathVariable(value = "id") long id){
        return "";
    }

    @GetMapping("/findAll")
    public String getAllActivities(@RequestHeader("Authorization") String authHeader){
        JSONObject result = new JSONObject();
        Long userId = jwtService.identifyUser(authHeader);
        return activityService.findAllActivityByUserId(userId);
    }
}
