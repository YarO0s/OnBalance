package com.denisov.onbalance.auth.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("test")
public class SecuredTestEndpointController {

    @GetMapping
    public String test(){
        return "Access granted";
    }
}
