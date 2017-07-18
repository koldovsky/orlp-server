package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.Folder;
import com.softserve.academy.spaced.repetition.domain.User;
import org.springframework.hateoas.Link;

public class UserPublicDTO extends DTO<User> {

    public UserPublicDTO(User user, Link link) {
        super(user, link);
    }

    public String getFirstName() {
        return getEntity().getPerson().getFirstName();
    }

    public String getLastName() {
        return getEntity().getPerson().getLastName();
    }

    public String getEmail() {
        return getEntity().getAccount().getEmail();
    }

    public AccountStatus getAccountStatus() {
        return getEntity().getAccount().getStatus();
    }

    /*
    Problems:
    2017-07-18 14:34:07.682  WARN 3120 --- [nio-8080-exec-3] .w.s.m.s.DefaultHandlerExceptionResolver : Failed to write HTTP message: org.springframework.http.converter.HttpMessageNotWritableException: Could not write content: No serializer found for class org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: java.util.ArrayList[3]->com.softserve.academy.spaced.repetition.DTO.impl.UserPublicDTO["usersFolder"]->com.softserve.academy.spaced.repetition.domain.Folder["decks"]->org.hibernate.collection.internal.PersistentBag[0]->com.softserve.academy.spaced.repetition.domain.Deck["category"]->com.softserve.academy.spaced.repetition.domain.Category_$$_jvst943_8["handler"]); nested exception is com.fasterxml.jackson.databind.JsonMappingException: No serializer found for class org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: java.util.ArrayList[3]->com.softserve.academy.spaced.repetition.DTO.impl.UserPublicDTO["usersFolder"]->com.softserve.academy.spaced.repetition.domain.Folder["decks"]->org.hibernate.collection.internal.PersistentBag[0]->com.softserve.academy.spaced.repetition.domain.Deck["category"]->com.softserve.academy.spaced.repetition.domain.Category_$$_jvst943_8["handler"])
2017-07-18 14:34:07.685  WARN 3120 --- [nio-8080-exec-3] .w.s.m.s.DefaultHandlerExceptionResolver : Handling of [org.springframework.http.converter.HttpMessageNotWritableException] resulted in Exception

java.lang.IllegalStateException: Cannot call sendError() after the response has been committed

     */
//    public Folder getUsersFolder() {
//        return getEntity().getFolder();
//    }

}
