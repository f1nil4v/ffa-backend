package com.ffa.tournaments.identity.application.service;

import com.ffa.tournaments.identity.application.port.EmailSender;
import com.ffa.tournaments.identity.domain.model.Account;
import com.ffa.tournaments.identity.domain.model.EmailVerificationToken;
import com.ffa.tournaments.identity.domain.repository.AccountRepository;
import com.ffa.tournaments.identity.domain.repository.EmailVerificationTokenRepository;
import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.identity.domain.valueobject.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.util.List;

@Service
public class EmailVerificationService {

    private final AccountRepository accountRepository;
    private final EmailVerificationTokenRepository tokenRepository;
    private final TokenService tokenService;
    private final EmailSender emailSender;
    private final String frontendUrl;

    public EmailVerificationService(AccountRepository accountRepository,
                                    EmailVerificationTokenRepository tokenRepository,
                                    TokenService tokenService,
                                    EmailSender emailSender,
                                    @Value("${app.frontend-url}") String frontendUrl) {
        this.accountRepository = accountRepository;
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
        this.emailSender = emailSender;
        this.frontendUrl = frontendUrl;
    }

    @Transactional
    public void createAndSendToken(Account account) {
        String rawToken = tokenService.generateToken();
        String tokenHash = tokenService.hashToken(rawToken);

        EmailVerificationToken token = EmailVerificationToken.create(account.getId(), tokenHash, Instant.now().plusSeconds(86400));
        tokenRepository.save(token);

        String link = frontendUrl + "/verify-email?token=" + rawToken;
        String text = "Please verify your email address by opening this link:\n\n" + link;

        sendAfterCommit(account.getEmail().value(), "Verify your email", text);
    }

    @Transactional
    public void verifyEmail(String rawToken) {
        String tokenHash = tokenService.hashToken(rawToken);

        EmailVerificationToken token = tokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired verification token"));

        if (!token.isValid()) {
            throw new IllegalArgumentException("Invalid or expired verification token");
        }

        Account account = accountRepository.findById(token.getAccountId())
                .orElseThrow(() -> new IllegalStateException("Account not found"));

        account.verifyEmail();
        token.markAsUsed();

        accountRepository.save(account);
        tokenRepository.save(token);
    }

    @Transactional
    public void resendVerificationEmail(String email) {
        Email normalizedEmail = new Email(email);

        Account account = accountRepository.findByEmail(normalizedEmail).orElse(null);

        if (account == null || account.isEmailVerified()) {
            return;
        }

        List<EmailVerificationToken> tokens = tokenRepository.findAllUnusedByAccountId(account.getId());

        for (EmailVerificationToken token : tokens) {
            token.markAsUsed();
            tokenRepository.save(token);
        }

        createAndSendToken(account);
    }

    private void sendAfterCommit(String to, String subject, String text) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    emailSender.send(to, subject, text);
                }
            });
        } else {
            emailSender.send(to, subject, text);
        }
    }
}