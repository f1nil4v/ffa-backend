package com.ffa.tournaments.identity.application.service;

import com.ffa.tournaments.identity.application.command.LoginCommand;
import com.ffa.tournaments.identity.application.command.RegisterCommand;
import com.ffa.tournaments.identity.application.exception.EmailAlreadyRegisteredException;
import com.ffa.tournaments.identity.application.exception.EmailNotVerifiedException;
import com.ffa.tournaments.identity.application.exception.InvalidCredentialsException;
import com.ffa.tournaments.identity.application.port.PasswordHasher;
import com.ffa.tournaments.identity.application.port.TokenIssuer;
import com.ffa.tournaments.identity.application.result.LoginResult;
import com.ffa.tournaments.identity.application.result.RegisterResult;
import com.ffa.tournaments.identity.domain.model.Account;
import com.ffa.tournaments.identity.domain.repository.AccountRepository;
import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.identity.domain.valueobject.Email;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AccountRepository accountRepository;
    private final PasswordHasher passwordHasher;
    private final TokenIssuer tokenIssuer;
    private final EmailVerificationService emailVerificationService;

    public AuthService(AccountRepository accountRepository, PasswordHasher passwordHasher, TokenIssuer tokenIssuer, EmailVerificationService emailVerificationService) {
        this.accountRepository = accountRepository;
        this.passwordHasher = passwordHasher;
        this.tokenIssuer = tokenIssuer;
        this.emailVerificationService = emailVerificationService;
    }

    @Transactional
    public RegisterResult register(RegisterCommand command) {
        Email email = new Email(command.email());

        if (accountRepository.existsByEmail(email)) {
            throw new EmailAlreadyRegisteredException();
        }
        String passwordHash = passwordHasher.hash(command.password());
        Account account = Account.register(
                AccountId.generate(),
                email,
                passwordHash
        );
        Account savedAccount = accountRepository.save(account);
        emailVerificationService.createAndSendToken(savedAccount);
        return new RegisterResult(
                savedAccount.getId().value(),
                savedAccount.getEmail().value(),
                savedAccount.isEmailVerified()
        );
    }

    @Transactional(readOnly = true)
    public LoginResult login(LoginCommand command) {
        Email email = new Email(command.email());

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordHasher.matches(command.password(), account.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        if (!account.isEmailVerified()) {
            throw new EmailNotVerifiedException();
        }

        String token = tokenIssuer.generateToken(account.getId());

        return new LoginResult(account.getId().value(), account.getEmail().value(), token, "Bearer");
    }
}