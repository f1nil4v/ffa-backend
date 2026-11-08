package com.ffa.tournaments.identity.application.service;

import com.ffa.tournaments.identity.application.port.EmailSender;
import com.ffa.tournaments.identity.application.port.PasswordHasher;
import com.ffa.tournaments.identity.domain.model.Account;
import com.ffa.tournaments.identity.domain.model.PasswordResetToken;
import com.ffa.tournaments.identity.domain.repository.AccountRepository;
import com.ffa.tournaments.identity.domain.repository.PasswordResetTokenRepository;
import com.ffa.tournaments.identity.domain.valueobject.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.util.List;

@Service
public class PasswordResetService {

    private final AccountRepository accountRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final TokenService tokenService;
    private final EmailSender emailSender;
    private final PasswordHasher passwordHasher;
    private final String frontendUrl;

    public PasswordResetService(AccountRepository accountRepository,
                                PasswordResetTokenRepository tokenRepository,
                                TokenService tokenService,
                                EmailSender emailSender,
                                PasswordHasher passwordHasher,
                                @Value("${app.frontend-url}") String frontendUrl) {
        this.accountRepository = accountRepository;
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
        this.emailSender = emailSender;
        this.passwordHasher = passwordHasher;
        this.frontendUrl = frontendUrl;
    }

    @Transactional
    public void requestPasswordReset(String email) {
        Email normalizedEmail = new Email(email);

        Account account = accountRepository.findByEmail(normalizedEmail).orElse(null);

        if (account == null) {
            return;
        }

        invalidateUnusedTokens(account);

        String rawToken = tokenService.generateToken();
        String tokenHash = tokenService.hashToken(rawToken);

        PasswordResetToken token = PasswordResetToken.create(
                account.getId(),
                tokenHash,
                Instant.now().plusSeconds(900)
        );

        tokenRepository.save(token);

        String link = frontendUrl + "/reset-password?token=" + rawToken;
        String text = "Reset your password by opening this link:\n\n" + link;

        sendAfterCommit(account.getEmail().value(), "Password reset", text);
    }

    @Transactional
    public void resetPassword(String rawToken, String newPassword) {
        String tokenHash = tokenService.hashToken(rawToken);

        PasswordResetToken token = tokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired password reset token"));

        if (!token.isValid()) {
            throw new IllegalArgumentException("Invalid or expired password reset token");
        }

        Account account = accountRepository.findById(token.getAccountId())
                .orElseThrow(() -> new IllegalStateException("Account not found"));

        account.changePassword(passwordHasher.hash(newPassword));

        invalidateUnusedTokens(account);
        accountRepository.save(account);
    }

    private void invalidateUnusedTokens(Account account) {
        List<PasswordResetToken> tokens = tokenRepository.findAllUnusedByAccountId(account.getId());

        for (PasswordResetToken token : tokens) {
            token.markAsUsed();
            tokenRepository.save(token);
        }
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