package com.denisov.onbalance.auth.user;

import com.denisov.onbalance.auth.confirmation.ConfirmationTokenEntity;
import com.denisov.onbalance.auth.confirmation.ConfirmationTokenRepository;
import com.denisov.onbalance.email.EmailService;
import com.denisov.onbalance.security.BCryptSecretEncryption;
import com.denisov.onbalance.security.SecretEncryption;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
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
    //TODO: token creation to separate routine
    //TODO: token message sending to separate routine
    public String registerUser(UserEntity userToAdd){
        JSONObject response = new JSONObject();

        String token;
        secretEncoder = new BCryptSecretEncryption();
        String email = userToAdd.getEmail();
        String name = userToAdd.getName();


        //TODO: decompose validation process
        if(userToAdd.getName() == "" || userToAdd.getName() == null || userToAdd.getName().length() > 20 ||
           userToAdd.getPassword() == "" || userToAdd.getPassword() == null || userToAdd.getPassword().length() > 20 ||
           userToAdd.getEmail() == null || userToAdd.getEmail() == ""){
            response.put("result", false);
            response.put("message", "provided data not valid");
            return response.toString();
        }


        Optional<UserEntity> optRegisteredUserEntity = userRepository.findByName(name);
        if(optRegisteredUserEntity.isPresent()){
            UserEntity registeredUserEntity = optRegisteredUserEntity.get();
            if(registeredUserEntity != null) {
                ConfirmationTokenEntity registeredConfirmationToken = tokenRepository.findByAppUserId(registeredUserEntity).get();
                if(registeredConfirmationToken.getExpirationTime().isBefore(LocalDateTime.now())){
                    if(registeredConfirmationToken.getConfirmationTime() == null &&
                       userToAdd.getEmail().compareTo(registeredUserEntity.getEmail()) != 0){
                        tokenRepository.removeByUserId(registeredUserEntity);
                        userRepository.delete(registeredUserEntity);
                    }
                }
            }
        }

        try {
            //put user into db
            userToAdd.setPassword(secretEncoder.encrypt(userToAdd.getPassword()));
            userRepository.save(userToAdd);

            //generate and put token into db
            token = RandomStringUtils.random(5, "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
            LocalDateTime currentTime = LocalDateTime.now();
            ConfirmationTokenEntity tokenEntity = new ConfirmationTokenEntity(token, currentTime, null,
                    currentTime.plusMinutes(5), userToAdd);
            tokenRepository.save(tokenEntity);

        } catch(DataIntegrityViolationException sqlException){
            sqlException.printStackTrace();
            String message = sqlException.getRootCause().getMessage();
            response.put("result", false);
            response.put("message", "" + message.substring(message.indexOf("(") + 1, message.indexOf(")")) + " already used");
            return response.toString();
        }

        try {
            emailService.sendEmail(email, token);
        } catch(Exception e){
            response.put("result", false);
            response.put("message", "provided email is not valid");
            return response.toString();
        }
        JSONObject obj = new JSONObject();
        response.put("result", true);
        response.put("message", "user registered");
        return response.toString();
    }

    public String confirmUser(String username, String token){
        JSONObject response = new JSONObject();
        UserEntity appUser = null;
        ConfirmationTokenEntity confirmationTokenEntity = null;
        Optional<UserEntity> optUser = userRepository.findByName(username);


        //TODO: decompose validation process
        if(username == null || username == "" || username.length() > 20 ||
           token == null || token == ""){
            response.put("result", false);
            response.put("message", "provided data not valid");
            return response.toString();
        }



        if(!optUser.isPresent()){
            response.put("result", false);
            response.put("message", "user not found");
            return response.toString();
        } else {
            appUser = optUser.get();
        }

        if(appUser == null){
            response.put("result", false);
            response.put("message", "user not found");
            return response.toString();
        }

        String email = appUser.getEmail();
        Optional<ConfirmationTokenEntity> optionalConfirmationToken = tokenRepository.findByAppUserId(appUser);

        if(!optionalConfirmationToken.isPresent()){
            response.put("result", false);
            response.put("message", "token not found");
            return response.toString();
        } else {
            confirmationTokenEntity = optionalConfirmationToken.get();
        }

        if(confirmationTokenEntity.getConfirmationTime()!=null){
            response.put("result", false);
            response.put("message", "account have been confirmed or name taken by another user");
            return response.toString();
        }

        LocalDateTime confirmedAt = LocalDateTime.now();

        if(confirmedAt.isAfter(confirmationTokenEntity.getExpirationTime())){
            tokenRepository.removeByUserId(appUser);

            token = RandomStringUtils.random(5, "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
            LocalDateTime currentTime = LocalDateTime.now();
            ConfirmationTokenEntity tokenEntity = new ConfirmationTokenEntity(token, currentTime, null,
                    currentTime.plusMinutes(5), appUser);
            tokenRepository.save(tokenEntity);

            emailService.sendEmail(email, token);

            response.put("result", false);
            response.put("message", "token expired, new one was resend");
            return response.toString();
        }

        if(confirmationTokenEntity.getToken().compareTo(token) != 0){
            response.put("result", false);
            response.put("message", "wrong token");
            return response.toString();
        }

        tokenRepository.updateConfirmedAt(confirmedAt, confirmationTokenEntity.getId());
        response.put("result", true);
        response.put("message", "confirmation passed");
        return response.toString();
    }
}