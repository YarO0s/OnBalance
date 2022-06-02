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
        ArrayList<ActivityEntity> activityEntities = new ArrayList<>();
        Optional userOpt = userRepository.findById(userId);
        UserEntity userEntity = null;

        if(userOpt.isPresent()){
            userEntity = (UserEntity) userOpt.get();
        } else {
            return "error: user not found";
        }

        Iterator<ActivityEntity> activityIterator = activityRepository.findAllByUserId(userEntity).iterator();

        ActivityEntity activityEntity = new ActivityEntity(title, color, description, 0, userEntity);

        if(!activityValidationService.isValid(activityEntity)){
            return "error: activity is not valid";
        }

        if(activityIterator.hasNext()){
            activityEntities.add(activityIterator.next());
        }

        if(activityEntities.size() == 15){
            return "error: constraint (15 max activities) violated";
        }

        try {
            activityRepository.save(activityEntity);
        } catch (Exception e){
            e.printStackTrace();
            return "error: unknown error occured";
        }

        return "successful: activity saved";
    }

    public String removeActivityById(long id){
        Optional<ActivityEntity> activityToRemove = activityRepository.findById(id);
        Iterator<ActivityEntity> activities = activityRepository.findAll().iterator();
        ArrayList<ActivityEntity> activityEntities = new ArrayList<ActivityEntity>();
        if(activities.hasNext()) {
            activityEntities.add(activities.next());
        }

        if(activityEntities.size()==4){
            return "error: constraint (4 min activities) violated";
        }

        if(!activityToRemove.isPresent()){
            return "error: activity not found";
        }

        try{
            activityRepository.deleteById(id);
        } catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }


    //TODO: replace userRepository with userDefaultService;
    public String findAllActivityByUserId(Long id){
        JSONObject result = new JSONObject();
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        UserEntity userEntity = null;
        if(optionalUserEntity.isPresent()){
            userEntity = optionalUserEntity.get();
        } else{
            return "error: user not found";
        }

        ArrayList<ActivityEntity> activityEntities = new ArrayList<ActivityEntity>();
        Iterator<ActivityEntity> activities = activityRepository.findAllByUserId(userEntity).iterator();
        while(activities.hasNext()){
            activityEntities.add(activities.next());
        }
        if(activityEntities.size() == 0){
            result.put("result", "error: activities not found");
        }
        result.put("result", activityEntities);

        return result.toString();
    }

}
