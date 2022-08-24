package com.denisov.onbalance.activity;

import com.denisov.onbalance.auth.user.UserEntity;
import com.denisov.onbalance.auth.user.UserRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityValidationService activityValidationService;
    private final UserRepository userRepository;

    public ActivityService(ActivityRepository activityRepository, ActivityValidationService activityValidationService,
                           UserRepository userRepository){
        this.activityRepository = activityRepository;
        this.activityValidationService = activityValidationService;
        this.userRepository = userRepository;
    }

    public String saveActivity(String title, String color, String description, long userId){
        Optional userOpt = userRepository.findById(userId);
        JSONObject response = new JSONObject();
        UserEntity userEntity = null;


        if(userOpt.isPresent()){
            userEntity = (UserEntity) userOpt.get();
        } else {
            response.put("result", false);
            response.put("message", "user not found");
            return response.toString();
        }

        long activitiesAmount = activityRepository.countByUserId(userEntity);
        ActivityEntity activityEntity = new ActivityEntity(title, color, description, 0, userEntity);

        if(!activityValidationService.isValid(activityEntity)){
            response.put("result", false);
            response.put("message", "activity data is not valid");
            return response.toString();
        }

        if(activitiesAmount >= 15){
            response.put("result", false);
            response.put("message", "constraint of 15 max activities violated");
            return response.toString();
        }

        try {
            activityRepository.save(activityEntity);
        } catch (Exception e){
            e.printStackTrace();
            response.put("result", false);
            response.put("message", "internal server error");
            return response.toString();
        }
        response.put("result", true);
        response.put("message", "activity saved");
        return response.toString();
    }

    public String removeActivityById(long activityId, long userId){
        Optional userOpt = userRepository.findById(userId);
        JSONObject response = new JSONObject();
        UserEntity userEntity = null;

        if(userOpt.isPresent()){
            userEntity = (UserEntity) userOpt.get();
        } else {
            response.put("result", false);
            response.put("message", "user not found");
            return response.toString();
        }

        long activitiesAmount = activityRepository.countByUserId(userEntity);
        Optional<ActivityEntity> activityToRemove = activityRepository.findById(activityId);
        Iterator<ActivityEntity> activities = activityRepository.findAllByUserId(userEntity).iterator();


        //TODO: If user wants to delete some activity while having only 4 of them, offer to recreate or smth
        if(activitiesAmount <= 4){
            response.put("result", false);
            response.put("message", "constraint of 4 min activities violated");
            return response.toString();
        }

        if(!activityToRemove.isPresent()){
            response.put("result", false);
            response.put("message", "activity not found");
            return response.toString();
        }

        ActivityEntity activity = activityToRemove.get();

        if(activity.getUserId().getId() != userId){
            response.put("result", false);
            response.put("message", "access denied");
            return response.toString();
        }

        try{
            activityRepository.deleteById(activityId);
        } catch(Exception e){
            e.printStackTrace();
            response.put("result", false);
            response.put("message", "internal server error");
            return response.toString();
        }

        response.put("result", false);
        response.put("message", "activity removed");
        return response.toString();
    }


    //TODO: replace userRepository with userDefaultService;
    public String findAllActivityByUserId(Long id){
        JSONObject response = new JSONObject();
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        UserEntity userEntity = null;
        if(optionalUserEntity.isPresent()){
            userEntity = optionalUserEntity.get();
        } else{
            response.put("result", false);
            response.put("message", "user not found");
            return response.toString();
        }

        ArrayList<ActivityEntity> activityEntities = new ArrayList<ActivityEntity>();
        ArrayList<ActivityResponse> activityResponseArray = new ArrayList<ActivityResponse>();
        Iterator<ActivityEntity> activities = activityRepository.findAllByUserId(userEntity).iterator();
        while(activities.hasNext()){
            ActivityEntity entity = activities.next();
            activityResponseArray.add(new ActivityResponse(entity.getId(), entity.getCompletionStatus(), entity.getName(), entity.getColor(), entity.getDescription()));
        }
        if(activityResponseArray.size() == 0){
            response.put("result", false);
            response.put("message", "activities not found");
            return response.toString();
        }
        response.put("result", true);
        response.put("message", "activities found");
        response.put("activities", activityResponseArray);

        return response.toString();
    }

    public String findActivityById(long activityId, long userId){
        Optional<UserEntity> optUserEntity = userRepository.findById(userId);
        JSONObject response = new JSONObject();
        if(!optUserEntity.isPresent()){
            response.put("result", false);
            response.put("message", "user not found");
            return response.toString();
        }

        Optional<ActivityEntity> optActivity = activityRepository.findById(activityId);
        if(!optActivity.isPresent()){
            response.put("result", false);
            response.put("message", "related activity does not exist");
            return response.toString();
        }

        ActivityEntity activity = optActivity.get();
        if(activity.getUserId().getId() != userId){
            response.put("result", false);
            response.put("message", "access denied");
            return response.toString();
        }

        ActivityResponse activityResponse = new ActivityResponse(activity.getId(), activity.getCompletionStatus(),
                                                                 activity.getName(), activity.getColor(), activity.getDescription());

        response.put("result", true);
        response.put("message", "activity found");
        response.put("activities", JSONObject.wrap(activityResponse));
        return response.toString();
    }

    public String updateActivity(long activityToUpdateId, String title, String color,
                                 String description, int completionStatus, long userId){
        Optional<UserEntity> optUserEntity = userRepository.findById(userId);
        JSONObject response = new JSONObject();
        if(!optUserEntity.isPresent()){
            response.put("result", false);
            response.put("message", "user not found");
            return response.toString();
        }

        Optional<ActivityEntity> optActivity = activityRepository.findById(activityToUpdateId);
        if(!optActivity.isPresent()){
            response.put("result", false);
            response.put("message", "activity not found");
            return response.toString();
        }

        ActivityEntity activity = optActivity.get();
        if(activity.getUserId().getId() != userId){
            response.put("result", false);
            response.put("message", "access denied");
            return response.toString();
        }

        if(!activityValidationService.isValid(new ActivityEntity(title, color, description, completionStatus, optUserEntity.get()))){
            response.put("result", false);
            response.put("message", "activity not valid");
            return response.toString();
        }

        try{
            activityRepository.update(title, color, description, completionStatus, activityToUpdateId);
        } catch(Exception e){
            e.printStackTrace();
            response.put("result", false);
            response.put("message", "internal server error");
            return response.toString();
        }

        response.put("result", true);
        response.put("message", "activity updated");
        return response.toString();
    }
}
