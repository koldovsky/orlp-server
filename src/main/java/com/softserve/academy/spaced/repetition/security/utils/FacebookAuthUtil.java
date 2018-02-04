package com.softserve.academy.spaced.repetition.security.utils;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Folder;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.domain.enums.ImageType;
import com.softserve.academy.spaced.repetition.repository.AccountRepository;
import com.softserve.academy.spaced.repetition.repository.AuthorityRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.service.AccountService;
import com.softserve.academy.spaced.repetition.service.UserService;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class FacebookAuthUtil {

    @Autowired
    private MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;

    public String getFBGraph(String accessToken) {
        String graph = null;

        try {
            String facebookGraph = "https://graph.facebook.com/me?fields=id,first_name,last_name,email,picture&access_token="
                    + accessToken;

            URL url = new URL(facebookGraph);

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
            throw new RuntimeException(messageSource.getMessage("message.exception.facebookDataGetError",
                    new Object[]{e}, locale));
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
            fbProfile.put("picture", json.getJSONObject("picture").getJSONObject("data").getString("url"));
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(messageSource.getMessage("message.exception.facebookDataParseError",
                    new Object[]{e}, locale));
        }

        return fbProfile;
    }

    public boolean checkIfExistUser(String email) {
        Account account = accountRepository.findByEmail(email);
        return account != null;
    }

    @Auditable(action = AuditingAction.SIGN_UP_FACEBOOK)
    public void saveNewFacebookUser(Map fbProfileData) {
        Account account = new Account();
        userService.initializeNewUser(account, (String) fbProfileData.get("email"), AccountStatus.ACTIVE,
                false, AuthenticationType.FACEBOOK);
        Person person = new Person((String) fbProfileData.get("first_name"), (String) fbProfileData.get("last_name"),
                ImageType.LINK, (String) fbProfileData.get("picture"));
        userRepository.save(new User(account, person, new Folder()));
        accountService.initializeLearningRegimeSettingsForAccount(account);
    }
}
