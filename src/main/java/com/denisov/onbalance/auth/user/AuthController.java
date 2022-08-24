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
    private final AuthenticationService authService;

    public AuthController(UserRegistrationService userRegistrationService,
                          AuthenticationService authService){
        this.userRegistrationService = userRegistrationService;
        this.authService = authService;
    }

    @PostMapping("/new")
    public String registerUser(@RequestParam("name")String name,
                               @RequestParam("email")String email,
                               @RequestParam("password")String password){
        UserEntity user = new UserEntity(name, email, password);
        return userRegistrationService.registerUser(user);
    }

    @PostMapping("/confirm")
    public String confirmUser(@RequestParam("username") String username, @RequestParam("token") String token){
        return userRegistrationService.confirmUser(username, token);
    }

    @PostMapping("/login")
    public String authenticate(@RequestParam(name = "username")String username,
                               @RequestParam(name = "password")String password){
        return authService.authenticate(username, password);
    }

    @PostMapping("/logout")
    public String logout(@RequestParam(name = "username")String username,
                         @RequestParam(name = "password")String password){
        //return authService.logout()
        return "";
    }
}

