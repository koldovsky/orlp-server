package com.softserve.academy.spaced.repetition.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.softserve.academy.spaced.repetition.audit.Auditable;
import com.softserve.academy.spaced.repetition.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.AccountRepository;
import com.softserve.academy.spaced.repetition.repository.AuthorityRepository;
import com.softserve.academy.spaced.repetition.repository.RememberingLevelRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.service.AccountService;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
public class GoogleAuthUtil {

    private final String FIRST_NAME = "given_name";
    private final String LAST_NAME = "family_name";
    private final String IMAGE = "picture";

    @Value("${app.social.google.client-id}")
    private String clientId;

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final AccountService accountService;

    private final UserService userService;

    @Autowired
    public GoogleAuthUtil(AccountRepository accountRepository, UserRepository userRepository,
                          AuthorityRepository authorityRepository,
                          RememberingLevelRepository rememberingLevelRepository, AccountService accountService, UserService userService) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.accountService = accountService;
        this.userService = userService;
    }

    public GoogleIdToken getGoogleIdToken(String idToken) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singleton(clientId)).build();
        try {
            return verifier.verify(idToken);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getEmail(GoogleIdToken googleIdToken) {
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        return payload.getEmail();
    }

    public boolean checkIfExistUser(String email) {
        Account account = accountRepository.findByEmail(email);
        return account != null;
    }

    public String getFirstName(GoogleIdToken googleIdToken) {
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        return (String) payload.get(FIRST_NAME);
    }

    public String getLastName(GoogleIdToken googleIdToken) {
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        return (String) payload.get(LAST_NAME);
    }

    @Auditable(action = AuditingAction.SIGN_UP_GOOGLE)
    public void saveNewGoogleUser(GoogleIdToken googleIdToken) {
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        Account account = new Account();
        userService.initializeNewUser(account, payload.getEmail(), AccountStatus.ACTIVE,false, AuthenticationType.GOOGLE);
        Person person = new Person((String) payload.get(FIRST_NAME), (String) payload.get(LAST_NAME), ImageType.LINK,
                (String) payload.get(IMAGE));
        userRepository.save(new User(account, person, new Folder()));
        accountService.initializeLearningRegimeSettingsForAccount(account);
    }
}
