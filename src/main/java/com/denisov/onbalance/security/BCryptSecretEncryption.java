package com.denisov.onbalance.security;

import com.denisov.onbalance.auth.user.UserEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptSecretEncryption implements SecretEncryption{
    @Override
    public String encrypt(String secret){
        String hash = BCrypt.hashpw(secret, BCrypt.gensalt());
        return hash;
    }

    @Override
    public boolean checkValidity(String secret, UserEntity user){
        return BCrypt.checkpw(secret, user.getPassword());
    }
}
