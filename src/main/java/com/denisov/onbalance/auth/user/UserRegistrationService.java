package com.denisov.onbalance.auth.user;

import com.denisov.onbalance.auth.confirmation.ConfirmationTokenEntity;
import com.denisov.onbalance.auth.confirmation.ConfirmationTokenRepository;
import com.denisov.onbalance.email.EmailService;
import com.denisov.onbalance.security.BCryptSecretEncryption;
import com.denisov.onbalance.security.SecretEncryption;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserRegistrationService {
    private final UserRepository userRepository;
    private final ConfirmationTokenRepository tokenRepository;
    private final EmailService emailService;
    private SecretEncryption secretEncoder;

    public UserRegistrationService(UserRepository userRepository, ConfirmationTokenRepository tokenRepository, EmailService emailService){
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    //TODO: Validate data and split logic to services and call them all from controller
    public String registerUser(UserEntity userToAdd){
        String token;
        secretEncoder = new BCryptSecretEncryption();
        String email = userToAdd.getEmail();
        try {
            //put user into db
            userToAdd.setPassword(secretEncoder.encrypt(userToAdd.getPassword()));
            userRepository.save(userToAdd);

            //generate and put token into db
            token = RandomStringUtils.random(5, "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
            LocalDateTime currentTime = LocalDateTime.now();
            ConfirmationTokenEntity tokenEntity = new ConfirmationTokenEntity(token, currentTime, null,
                    currentTime.plusMinutes(10), userToAdd);
            tokenRepository.save(tokenEntity);

        } catch(DataIntegrityViolationException sqlException){
            sqlException.printStackTrace();
            String message = sqlException.getRootCause().getMessage();
            return "error: " + message.substring(message.indexOf("(") + 1, message.indexOf(")")) + " already used";
        }
        try {
            emailService.sendEmail(email, token);
        } catch(Exception e){
            return"error: provided email is not valid";
        }
        return "successful: user registered";
    }

    public String confirmUser(String token){


        return "successful: ";
    }
}