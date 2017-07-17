package com.softserve.academy.spaced.repetition.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.AccountRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;

@Component
public class GoogleAuthUtil {

    @Value("${spring.social.google.client-id}")
    private String clientId;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

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
        return (String) payload.get("given_name");
    }

    public String getLastName(GoogleIdToken googleIdToken) {
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        return (String) payload.get("family_name");
    }

    public void saveNewGoogleUser(GoogleIdToken googleIdToken) {
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        User user = new User();
        Account account = new Account();
        Folder folder = new Folder();
        Person person = new Person();
        account.setEmail(payload.getEmail());
        account.setPassword("-1");
        account.setLastPasswordResetDate(new Date());
        account.setStatus(AccountStatus.ACTIVE);
        account.setAuthorities(Collections.singleton(new Authority(AuthorityName.ROLE_USER)));
        person.setFirstName((String) payload.get("given_name"));
        person.setLastName((String) payload.get("family_name"));
        user.setAccount(account);
        user.setFolder(folder);
        user.setPerson(person);
        userRepository.save(user);
    }
}
