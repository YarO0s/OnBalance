package com.denisov.onbalance.auth.user;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "auth", produces = "application/json")
public class AuthController {
    private final UserRegistrationService userRegistrationService;

    public AuthController(UserRegistrationService userRegistrationService){
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping("/new")
    public String registerUser(@RequestParam("name")String name,
                               @RequestParam("email")String email,
                               @RequestParam("password")String password){
        UserEntity user = new UserEntity(name, email, password);
        return new JSONObject().put("result", ""+ userRegistrationService.registerUser(user)).toString();
    }

    @PostMapping("/confirm")
    public String confirmUser(@RequestParam("token")String token){
        return new JSONObject().put("result", userRegistrationService.confirmUser(token)).toString();
    }
}
