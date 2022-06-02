package com.denisov.onbalance.auth.user;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.denisov.onbalance.security.JWTService;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController()
@RequestMapping("test")
public class SecuredTestEndpointController {
    private final UserRepository userRepository;
    private final JWTService jwtService;

    public SecuredTestEndpointController(UserRepository userRepository,
                                         JWTService jwtService){
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @GetMapping
    public String test(@RequestHeader("Authorization") String authHeader){
        long userId = jwtService.identifyUser(authHeader);

        Optional<UserEntity> optUser = userRepository.findById(userId);
        return "Access granted to: " + userId;
    }
}
