package com.softserve.academy.spaced.repetition.security;

import com.softserve.academy.spaced.repetition.audit.Auditable;
import com.softserve.academy.spaced.repetition.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.AccountRepository;
import com.softserve.academy.spaced.repetition.repository.AuthorityRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class FacebookAuthUtil {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    public String getFBGraph(String accessToken) {
        String graph = null;

        try {
            String g = "https://graph.facebook.com/me?fields=id,first_name,last_name,email&access_token=" + accessToken;

            URL url = new URL(g);

            URLConnection urlConnection = url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuffer userInfo = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                userInfo.append(inputLine + "\n");
            }
            in.close();

            graph = userInfo.toString();
            System.out.println(graph);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ERROR in getting FB graph data. " + e);
        }

        return graph;
    }

    public Map getGraphData(String fbGraph) {
        Map fbProfile = new HashMap();

        try {
            JSONObject json = new JSONObject(fbGraph);
            fbProfile.put("first_name", json.getString("first_name"));
            fbProfile.put("email", json.getString("email"));
            fbProfile.put("last_name", json.getString("last_name"));
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("ERROR in parsing FB graph data. " + e);
        }

        return fbProfile;
    }

    public boolean checkIfExistUser(String email) {
        Account account = accountRepository.findByEmail(email);
        return account != null;
    }

    @Auditable(action = AuditingAction.SIGN_UP_FACEBOOK)
    public void saveNewFacebookUser(Map fbProfileData) {
        User user = new User();
        Account account = new Account();
        Folder folder = new Folder();
        Person person = new Person();
        account.setEmail((String) fbProfileData.get("email"));
        account.setPassword("-1");
        account.setLastPasswordResetDate(new Date());
        account.setStatus(AccountStatus.ACTIVE);
        Authority authority = authorityRepository.findAuthorityByName(AuthorityName.ROLE_USER);
        account.setAuthorities(Collections.singleton(authority));
        person.setFirstName((String) fbProfileData.get("first_name"));
        person.setLastName((String) fbProfileData.get("last_name"));
        user.setAccount(account);
        user.setFolder(folder);
        user.setPerson(person);
        userRepository.save(user);
    }
}
