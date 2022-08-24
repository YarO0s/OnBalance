package com.denisov.onbalance.task;

import com.denisov.onbalance.activity.ActivityEntity;
import com.denisov.onbalance.activity.ActivityRepository;
import com.denisov.onbalance.auth.user.UserEntity;
import com.denisov.onbalance.auth.user.UserRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final TaskValidationService taskValidationService;
    private final TaskRepository taskRepository;

    public TaskService(UserRepository userRepository, ActivityRepository activityRepository,
                       TaskValidationService taskValidationService, TaskRepository taskRepository){
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.taskValidationService = taskValidationService;
        this.taskRepository = taskRepository;
    }

    public String saveTask(long userId, long activityId, String title, String description){
        Optional<UserEntity> optUserEntity = userRepository.findById(userId);
        JSONObject response = new JSONObject();

        if(!optUserEntity.isPresent()){
            response.put("result", false);
            response.put("message", "user not found");
            return response.toString();
        }

        Optional<ActivityEntity> optActivityEntity = activityRepository.findById(activityId);
        if(!optActivityEntity.isPresent()){
            response.put("result", false);
            response.put("message", "activity not found");
            return response.toString();
        }

        ActivityEntity activity = optActivityEntity.get();
        if(activity.getUserId().getId() != optUserEntity.get().getId()){
            response.put("result", false);
            response.put("message", "permission denied");
            return response.toString();
        }

        TaskEntity newTask = new TaskEntity(title, description, false, activity);
        if(!taskValidationService.isValid(newTask)){
            response.put("result", false);
            response.put("message", "task validation failed");
            return response.toString();
        }

        try {
            taskRepository.save(newTask);
        } catch (Exception e){
            e.printStackTrace();
            response.put("result", false);
            response.put("message", "internal server error");
            return response.toString();
        }

        response.put("result", true);
        response.put("message", "task saved");
        return response.toString();
    }

    public String removeTask(long userId, long taskId){
        Optional<UserEntity> optUserEntity = userRepository.findById(userId);
        JSONObject response = new JSONObject();


        if(!optUserEntity.isPresent()){
            response.put("result", false);
            response.put("message", "user not found");
            return response.toString();
        }

        Optional<TaskEntity> optTaskEntity = taskRepository.findById(taskId);

        if(!optTaskEntity.isPresent()){
            response.put("result", false);
            response.put("message", "task not found");
            return response.toString();
        }

        TaskEntity task = optTaskEntity.get();
        Optional<ActivityEntity> optActivityEntity = activityRepository.findById(task.getActivityId().getId());

        if(!optActivityEntity.isPresent()){
            response.put("result", false);
            response.put("message", "related activity not found");
            return response.toString();
            //return "activity recognition error. Task may not be properly tied with activity";
        }

        if(optUserEntity.get().getId() != optActivityEntity.get().getUserId().getId()){
            response.put("result", false);
            response.put("message", "permission denied");
            return response.toString();
        }

        try{
            taskRepository.deleteById(taskId);
        } catch(Exception e){
            e.printStackTrace();
            response.put("result", false);
            response.put("message", "internal server error");
            return response.toString();
        }
        response.put("result", true);
        response.put("message", "task removed");
        return response.toString();
    }

    public String findTaskById(long userId, long taskId){
        Optional<UserEntity> optUserEntity = userRepository.findById(userId);
        JSONObject response = new JSONObject();


        if(!optUserEntity.isPresent()){
            response.put("result", false);
            response.put("message", "user not found");
            return response.toString();
        }

        Optional<TaskEntity> optTaskEntity = taskRepository.findById(taskId);

        if(!optTaskEntity.isPresent()){
            response.put("result", false);
            response.put("message", "task not found");
            return response.toString();
        }

        TaskEntity task = optTaskEntity.get();
        Optional<ActivityEntity> optActivityEntity = activityRepository.findById(task.getActivityId().getId());

        if(!optActivityEntity.isPresent()){
            response.put("result", false);
            response.put("message", "related activity not found");
            return response.toString();
        }

        if(optUserEntity.get().getId() != optActivityEntity.get().getUserId().getId()){
            response.put("result", false);
            response.put("message", "permission denied");
            return response.toString();
        }

        TaskResponse taskResponse = new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus());

        response.put("result", true);
        response.put("message", "tasks found");
        response.put("tasks", JSONObject.wrap(taskResponse));
        return response.toString();
    }

    public String findAllTasksByActivityId(long userId, long activityId){
        Optional<UserEntity> optUserEntity = userRepository.findById(userId);
        List<TaskEntity> tasks = new ArrayList<TaskEntity>();
        ArrayList<TaskResponse> tasksResponse = new ArrayList<TaskResponse>();
        JSONObject response = new JSONObject();


        if(!optUserEntity.isPresent()){
            response.put("result", false);
            response.put("message", "user not found");
            return response.toString();
        }

        Optional<ActivityEntity> optActivityEntity = activityRepository.findById(activityId);

        if(!optActivityEntity.isPresent()){
            response.put("result", false);
            response.put("message", "related activity not found");
            return response.toString();
        }

        if(optUserEntity.get().getId() != optActivityEntity.get().getUserId().getId()){
            response.put("result", false);
            response.put("message", "permission denied");
            return response.toString();
        }

        Iterator<TaskEntity> taskEntities = taskRepository.findAllByActivityId(optActivityEntity.get()).iterator();
        while(taskEntities.hasNext()){
            TaskEntity task = taskEntities.next();
            tasks.add(task);
            tasksResponse.add(new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus()));
        }
        if(tasks.size()==0){
            response.put("result", false);
            response.put("message", "task not found");
            return response.toString();
        }

        response.put("result", true);
        response.put("message", "tasks found");
        response.put("tasks", tasksResponse);
        return response.toString();
    }

    public String updateTask(long userId, long taskId, String title, String description, boolean status){
        Optional<UserEntity> optUserEntity = userRepository.findById(userId);
        JSONObject response = new JSONObject();


        if(!optUserEntity.isPresent()){
            response.put("result", false);
            response.put("message", "user not found");
            return response.toString();
        }

        Optional<TaskEntity> optTaskEntity = taskRepository.findById(taskId);

        if(!optTaskEntity.isPresent()){
            response.put("result", false);
            response.put("message", "task not found");
            return response.toString();
        }

        TaskEntity task = optTaskEntity.get();
        Optional<ActivityEntity> optActivityEntity = activityRepository.findById(task.getActivityId().getId());

        if(!optActivityEntity.isPresent()){
            response.put("result", false);
            response.put("message", "related activity not found");
            return response.toString();
        }

        if(optUserEntity.get().getId() != optActivityEntity.get().getUserId().getId()){
            response.put("result", false);
            response.put("message", "permission denied");
            return response.toString();
        }

        try {
            taskRepository.update(taskId, title, description, status);
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
