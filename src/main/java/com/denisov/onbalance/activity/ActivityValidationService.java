package com.denisov.onbalance.activity;

import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class ActivityValidationService {

    public ActivityValidationService(){
    }
    public boolean isValid(ActivityEntity activityEntity){
        int completion = activityEntity.getCompletionStatus();
        String colorCode = activityEntity.getColor();
        String name = activityEntity.getName();

        if(name.length()==0){
            return false;
        }

        if(completion<0 || completion>100){
            return false;
        }

        if(colorCode.length()!=6){
            return false;
        }

        colorCode = colorCode.toUpperCase();
        char[] splitColor = colorCode.toCharArray();

        for(int i = 0; i < splitColor.length; i++){
            if(!(splitColor[i]>=48 && splitColor[i]<=57 || splitColor[i]>=65 && splitColor[i]<=70)){
                return false;
            }
        }

        return true;
    }
}

