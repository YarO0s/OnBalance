package com.denisov.onbalance.task;

import com.denisov.onbalance.security.JWTService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="task", produces="application/json")
public class TaskController {
    private final JWTService jwtService;
    private final TaskRepository taskRepository;
    private final TaskService taskService;

    public TaskController(JWTService jwtService, TaskRepository taskRepository, TaskService taskService){
        this.jwtService = jwtService;
        this. taskRepository = taskRepository;
        this.taskService = taskService;
    }

    @PostMapping("/new")
    public String saveTask(@RequestHeader("Authorization") String authHeader,
                         String title, String description, long activityId){
        Long userId = jwtService.identifyUser(authHeader);
        return taskService.saveTask(userId, activityId, title, description);
    }

    @PostMapping("/update/{id}")
    public String updateTask(@RequestHeader("Authorization") String authHeader, @PathVariable("id") long taskId,
                             String title, String description, boolean status){
        Long userId = jwtService.identifyUser(authHeader);
        return taskService.updateTask(userId, taskId, title, description, status);
    }

    @GetMapping("/findAllByActivityId")
    public String findAllTasks(@RequestHeader("Authorization") String authHeader, Long activityId){
        Long userId = jwtService.identifyUser(authHeader);
        return taskService.findAllTasksByActivityId(userId, activityId);
    }

    @GetMapping("/find/{id}")
    public String findById(@RequestHeader("Authorization") String authHeader, @PathVariable("id") long taskId){
        Long userId = jwtService.identifyUser(authHeader);
        return taskService.findTaskById(userId, taskId);
    }

    @GetMapping("/removeTask/{id}")
    public String removeTask(@RequestHeader("Authorization") String authHeader, @PathVariable("id") long taskId){
        Long userId = jwtService.identifyUser(authHeader);
        return taskService.removeTask(userId, taskId);
    }
}
