package com.denisov.onbalance.task;

import org.springframework.stereotype.Service;

@Service
public class TaskValidationService {
    public boolean isValid(TaskEntity taskEntity){
        if(taskEntity.getTitle().length() <= 0){
            return false;
        }

        return true;
    }
}
