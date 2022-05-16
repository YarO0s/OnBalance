package com.denisov.onbalance.security;

import com.denisov.onbalance.auth.user.UserEntity;

public interface SecretEncryption {
    public String encrypt(String password);

    public boolean checkValidity(String secret, UserEntity user);
}
