package com.ffa.tournaments.support.infrastructure.bootstrap;

import com.ffa.tournaments.identity.domain.valueobject.Email;
import com.ffa.tournaments.support.domain.model.SupportAccount;
import com.ffa.tournaments.support.domain.repository.SupportAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SupportAccountBootstrap implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(SupportAccountBootstrap.class);

    private final SupportAccountRepository supportAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final String email;
    private final String password;
    private final String displayName;

    public SupportAccountBootstrap(
            SupportAccountRepository supportAccountRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.support.bootstrap.email:}") String email,
            @Value("${app.support.bootstrap.password:}") String password,
            @Value("${app.support.bootstrap.display-name:Support}") String displayName
    ) {
        this.supportAccountRepository = supportAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.email = email;
        this.password = password;
        this.displayName = displayName;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            log.info("Support bootstrap is not configured, skipping");
            return;
        }

        Email bootstrapEmail = new Email(email);

        if (supportAccountRepository.existsByEmail(bootstrapEmail)) {
            log.info("Support bootstrap account already exists, skipping");
            return;
        }

        SupportAccount account = SupportAccount.create(
                bootstrapEmail,
                passwordEncoder.encode(password),
                displayName
        );

        supportAccountRepository.save(account);

        log.info("Support bootstrap account created for {}", bootstrapEmail.value());
    }
}
