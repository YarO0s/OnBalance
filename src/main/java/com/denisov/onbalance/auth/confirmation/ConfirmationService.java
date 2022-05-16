package com.denisov.onbalance.auth.confirmation;

public class ConfirmationService {
    private final ConfirmationTokenRepository tokenRepository;
    public ConfirmationService(ConfirmationTokenRepository tokenRepository){
        this.tokenRepository = tokenRepository;
    }

    public String confirm(ConfirmationTokenRepository token){
        return "";
    }
}
