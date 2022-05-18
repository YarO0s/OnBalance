package com.denisov.onbalance.auth.user;

import com.denisov.onbalance.auth.confirmation.ConfirmationTokenEntity;
import com.denisov.onbalance.auth.confirmation.ConfirmationTokenRepository;
import com.denisov.onbalance.security.BCryptSecretEncryption;
import com.denisov.onbalance.security.JWTService;
import com.denisov.onbalance.security.SecretEncryption;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final ConfirmationTokenRepository tokenRepository;
    private final JWTService jwtService = new JWTService();

    private final SecretEncryption passwordEncoder = new BCryptSecretEncryption();

    public AuthenticationService(UserRepository userRepository,
                                 ConfirmationTokenRepository tokenRepository){
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    public String authenticate(String identifier, String secret){
        Optional<ConfirmationTokenEntity> optToken;
        UserEntity userEntity = identifyUserEntity(identifier);
        String jwt = "";

        try {
            if(userEntity == null){
                return "error: user not found";
            }

            optToken = tokenRepository.findByAppUserId(userEntity);

            if (optToken == null) {
                return "error: token not found";
            }

            ConfirmationTokenEntity confirmationToken = optToken.get();

            //TODO: regenerate token and send again
            if (confirmationToken.getConfirmationTime() == null) {
                return "error: account not confirmed";
            }

            //TODO:Email or nickname
            //TODO:All response in json
            String encryptedSecret = passwordEncoder.encrypt(secret);
            if (passwordEncoder.checkValidity(secret, userEntity)) {
                jwt = jwtService.generateJWT(userEntity);
            } else {
                return "error: password incorrect";
            }
        } catch(Exception e){
            e.printStackTrace();
            return "error: authentication service error";
        }
        return "successful: " + jwt;
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
