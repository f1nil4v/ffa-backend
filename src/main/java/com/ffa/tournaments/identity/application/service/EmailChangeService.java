package com.ffa.tournaments.identity.application.service;

import com.ffa.tournaments.identity.application.exception.EmailAlreadyRegisteredException;
import com.ffa.tournaments.identity.application.exception.EmailChangeRequestExpiredException;
import com.ffa.tournaments.identity.application.exception.EmailUnchangedException;
import com.ffa.tournaments.identity.application.exception.InvalidEmailChangeTokenException;
import com.ffa.tournaments.identity.application.port.EmailSender;
import com.ffa.tournaments.identity.application.result.EmailChangeRequestResult;
import com.ffa.tournaments.identity.application.result.EmailChangeStatusResult;
import com.ffa.tournaments.identity.domain.model.Account;
import com.ffa.tournaments.identity.domain.model.EmailChangeRequest;
import com.ffa.tournaments.identity.domain.repository.AccountRepository;
import com.ffa.tournaments.identity.domain.repository.EmailChangeRequestRepository;
import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.identity.domain.valueobject.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;

@Service
public class EmailChangeService {

    private static final long TOKEN_TTL_SECONDS = 86400;

    private final AccountRepository accountRepository;
    private final EmailChangeRequestRepository emailChangeRequestRepository;
    private final TokenService tokenService;
    private final EmailSender emailSender;
    private final String frontendUrl;

    public EmailChangeService(
            AccountRepository accountRepository,
            EmailChangeRequestRepository emailChangeRequestRepository,
            TokenService tokenService,
            EmailSender emailSender,
            @Value("${app.frontend-url}") String frontendUrl
    ) {
        this.accountRepository = accountRepository;
        this.emailChangeRequestRepository = emailChangeRequestRepository;
        this.tokenService = tokenService;
        this.emailSender = emailSender;
        this.frontendUrl = frontendUrl;
    }

    @Transactional
    public EmailChangeRequestResult request(AccountId accountId, String newEmail) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(IllegalStateException::new);

        Email targetEmail = new Email(newEmail);

        if (targetEmail.equals(account.getEmail())) {
            throw new EmailUnchangedException();
        }

        if (accountRepository.existsByEmail(targetEmail)) {
            throw new EmailAlreadyRegisteredException();
        }

        emailChangeRequestRepository.findPendingByAccountId(accountId)
                .ifPresent(pending -> {
                    pending.cancel();
                    emailChangeRequestRepository.save(pending);
                });

        String rawOldToken = tokenService.generateToken();
        String rawNewToken = tokenService.generateToken();

        EmailChangeRequest request = EmailChangeRequest.create(
                accountId,
                account.getEmail(),
                targetEmail,
                tokenService.hashToken(rawOldToken),
                tokenService.hashToken(rawNewToken),
                Instant.now().plusSeconds(TOKEN_TTL_SECONDS)
        );

        EmailChangeRequest savedRequest = emailChangeRequestRepository.save(request);

        String oldLink = buildConfirmationLink(rawOldToken);
        String newLink = buildConfirmationLink(rawNewToken);

        sendAfterCommit(
                account.getEmail().value(),
                "Confirm your email change",
                "You requested an email change. Confirm it by opening this link:\n\n" + oldLink
        );

        sendAfterCommit(
                targetEmail.value(),
                "Confirm your new email",
                "Confirm this address as your new email by opening this link:\n\n" + newLink
        );

        return EmailChangeRequestResult.from(savedRequest);
    }

    @Transactional
    public EmailChangeRequestResult confirm(String rawToken) {
        String tokenHash = tokenService.hashToken(rawToken);

        EmailChangeRequest request = emailChangeRequestRepository.findByTokenHash(tokenHash)
                .orElseThrow(InvalidEmailChangeTokenException::new);

        if (!request.isPending()) {
            throw new InvalidEmailChangeTokenException();
        }

        Instant now = Instant.now();

        if (request.isExpired(now)) {
            request.expire();
            emailChangeRequestRepository.save(request);
            throw new EmailChangeRequestExpiredException();
        }

        if (request.getOldTokenHash().equals(tokenHash)) {
            request.confirmOld(tokenHash, now);
        } else {
            request.confirmNew(tokenHash, now);
        }

        EmailChangeRequest savedRequest = emailChangeRequestRepository.save(request);

        if (!request.hasBothConfirmations()) {
            return EmailChangeRequestResult.from(savedRequest);
        }

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(IllegalStateException::new);

        if (accountRepository.existsByEmail(request.getNewEmail())) {
            throw new EmailAlreadyRegisteredException();
        }

        account.changeEmail(request.getNewEmail());
        account.verifyEmail();

        accountRepository.save(account);

        request.complete();
        savedRequest = emailChangeRequestRepository.save(request);

        String notification = "Your account email address has been changed to "
                + request.getNewEmail().value() + ".";

        sendAfterCommit(request.getCurrentEmail().value(), "Email address changed", notification);
        sendAfterCommit(request.getNewEmail().value(), "Email address changed", notification);

        return EmailChangeRequestResult.from(savedRequest);
    }

    @Transactional(readOnly = true)
    public EmailChangeStatusResult status(AccountId accountId) {
        return emailChangeRequestRepository.findPendingByAccountId(accountId)
                .map(EmailChangeStatusResult::from)
                .orElseGet(EmailChangeStatusResult::none);
    }

    private String buildConfirmationLink(String rawToken) {
        return frontendUrl + "/confirm-email-change?token=" + rawToken;
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
