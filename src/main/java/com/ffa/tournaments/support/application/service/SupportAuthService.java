package com.ffa.tournaments.support.application.service;

import com.ffa.tournaments.identity.application.port.PasswordHasher;
import com.ffa.tournaments.identity.domain.valueobject.Email;
import com.ffa.tournaments.support.application.exception.InvalidSupportCredentialsException;
import com.ffa.tournaments.support.application.port.SupportTokenIssuer;
import com.ffa.tournaments.support.application.result.SupportLoginResult;
import com.ffa.tournaments.support.domain.model.SupportAccount;
import com.ffa.tournaments.support.domain.repository.SupportAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SupportAuthService {

    private final SupportAccountRepository supportAccountRepository;
    private final PasswordHasher passwordHasher;
    private final SupportTokenIssuer tokenIssuer;

    public SupportAuthService(
            SupportAccountRepository supportAccountRepository,
            PasswordHasher passwordHasher,
            SupportTokenIssuer tokenIssuer
    ) {
        this.supportAccountRepository = supportAccountRepository;
        this.passwordHasher = passwordHasher;
        this.tokenIssuer = tokenIssuer;
    }

    @Transactional(readOnly = true)
    public SupportLoginResult login(String email, String password) {
        Email normalizedEmail = new Email(email);

        SupportAccount account = supportAccountRepository.findByEmail(normalizedEmail)
                .orElseThrow(InvalidSupportCredentialsException::new);

        if (!account.isActive()
                || !passwordHasher.matches(password, account.getPasswordHash())) {
            throw new InvalidSupportCredentialsException();
        }

        String token = tokenIssuer.issue(account.getId());

        return new SupportLoginResult(
                account.getId().value(),
                account.getEmail().value(),
                account.getDisplayName(),
                token,
                "Bearer"
        );
    }
}
