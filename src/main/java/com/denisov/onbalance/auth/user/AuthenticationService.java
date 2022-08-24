package com.denisov.onbalance.auth.user;

import com.denisov.onbalance.auth.confirmation.ConfirmationTokenEntity;
import com.denisov.onbalance.auth.confirmation.ConfirmationTokenRepository;
import com.denisov.onbalance.security.BCryptSecretEncryption;
import com.denisov.onbalance.security.JWTService;
import com.denisov.onbalance.security.SecretEncryption;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final ConfirmationTokenRepository tokenRepository;
    private final JWTService jwtService;
    private final SecretEncryption passwordEncoder = new BCryptSecretEncryption();

    public AuthenticationService(UserRepository userRepository,
                                 ConfirmationTokenRepository tokenRepository,
                                 JWTService jwtService){
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
    }

    public String authenticate(String identifier, String secret){
        Optional<ConfirmationTokenEntity> optToken;
        UserEntity userEntity = identifyUserEntity(identifier);
        JSONObject response = new JSONObject();
        String jwt = "";

        if(identifier == null || identifier == "" || secret == null || secret == ""){
            response.put("result", false);
            response.put("message", "provided data not valid");
        }

        try {
            if(userEntity == null){
                response.put("result", false);
                response.put("message", "user not found");
                return response.toString();
            }

            optToken = tokenRepository.findByAppUserId(userEntity);

            if (optToken == null) {
                response.put("result", false);
                response.put("message", "token not found");
                return response.toString();
            }

            ConfirmationTokenEntity confirmationToken = optToken.get();

            //TODO: regenerate token and send again
            if (confirmationToken.getConfirmationTime() == null) {
                response.put("result", false);
                response.put("message", "account not confirmed");
                return response.toString();
            }

            //TODO:Email or nickname
            //TODO:All response in json
            String encryptedSecret = passwordEncoder.encrypt(secret);
            if (passwordEncoder.checkValidity(secret, userEntity)) {
                jwt = jwtService.generateJWT(userEntity);
            } else {
                response.put("result", false);
                response.put("message", "incorrect password");
                return response.toString();
            }
        } catch(Exception e){
            e.printStackTrace();
            response.put("result", false);
            response.put("message", "auth service error");
            return response.toString();
        }
        response.put("result", true);
        response.put("id", userEntity.getId());
        response.put("name", userEntity.getName());
        response.put("email", userEntity.getEmail());
        response.put("message", "authenticated");
        response.put("token", jwt);
        return response.toString();
    }

    private UserEntity identifyUserEntity(String identifier){
        Optional<UserEntity> optUserByName = userRepository.findByName(identifier);
        Optional<UserEntity> optUserByEmail = userRepository.findByEmail(identifier);
        UserEntity entityToReturn = null;

        if(optUserByName.isPresent()){
            entityToReturn = optUserByName.get();
        } else if(optUserByEmail.isPresent()){
            entityToReturn = optUserByEmail.get();
        }

        return entityToReturn;
    }
}
