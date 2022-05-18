package com.denisov.onbalance.activity;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityValidationService activityValidationService;

    public ActivityService(ActivityRepository activityRepository, ActivityValidationService activityValidationService){
        this.activityRepository = activityRepository;
        this.activityValidationService = activityValidationService;
    }

    public String saveActivity(ActivityEntity activityEntity){
        Iterator<ActivityEntity> activityIterator = activityRepository.findAll().iterator();
        ArrayList<ActivityEntity> activityEntities = new ArrayList<>();

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

}
