package com.softserve.academy.spaced.repetition.config;

import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.domain.enums.AuthorityName;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventListenerBean {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    private static final Logger LOG = LoggerFactory.getLogger(EventListenerBean.class);

    @EventListener()
    public void handleCheckPointsEvent(ContextRefreshedEvent event) {
        LOG.info("Checking user points");
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getAccount().getAuthorities().stream().anyMatch(authority ->
                    authority.getName().equals(AuthorityName.ROLE_USER))) {
                userService.updatePointsBalance(user);
            } else {
                user.setPoints(0);
                userRepository.save(user);
            }
        }
    }
}


