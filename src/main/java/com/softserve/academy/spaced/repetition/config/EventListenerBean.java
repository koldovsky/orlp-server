package com.softserve.academy.spaced.repetition.config;

import com.softserve.academy.spaced.repetition.domain.Person;
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
import java.util.Optional;

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

    @EventListener
    public User updatePointsBalance(PointsBalanceEvent pointBalanceEvent) {
        String firstName = Optional.ofNullable(pointBalanceEvent.getUser().getPerson()).map(Person::getFirstName)
                .orElse(null);
        String lastName = Optional.ofNullable(pointBalanceEvent.getUser().getPerson()).map(Person::getLastName)
                .orElse(null);
        LOG.info("Updating points balance for: {} {}, {}", firstName, lastName,
                pointBalanceEvent.getClass().getCanonicalName());
        return userService.updatePointsBalance(pointBalanceEvent.getUser());
    }
}


