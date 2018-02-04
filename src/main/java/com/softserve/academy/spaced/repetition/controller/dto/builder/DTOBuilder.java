package com.softserve.academy.spaced.repetition.controller.dto.builder;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.EntityInterface;
import org.springframework.hateoas.Link;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DTOBuilder {

    public static <M extends EntityInterface, T extends DTO<M>> T buildDtoForEntity(M entity, Class<T> dtoClass) {
        Constructor<T> declaredConstructor = null;
        try {
            declaredConstructor = dtoClass.getDeclaredConstructor(entity.getClass());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        T newInstance = null;
        try {
            newInstance = declaredConstructor.newInstance(entity);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return newInstance;
    }

    public static <M extends EntityInterface, T extends DTO<M>> T buildDtoForEntity(M entity, Class<T> dtoClass, Link selfLink) {
        if(entity == null){
            return null;
        }
        Constructor<T> declaredConstructor = null;
        try {
            declaredConstructor = dtoClass.getDeclaredConstructor(entity.getClass(), selfLink.getClass());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        T newInstance = null;
        try {
            newInstance = declaredConstructor.newInstance(entity, selfLink.withSelfRel());
        } catch (NullPointerException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return newInstance;
    }

    public static <M extends EntityInterface, T extends DTO<M>> List<T> buildDtoListForCollection(List<M> collection, Class<T> dtoClass) {
        List<T> result = new ArrayList<>();
        if (collection == null) {
            return result;
        }
        for (M document : collection) {
            T dtoForEntity = buildDtoForEntity(document, dtoClass);
            result.add(dtoForEntity);
        }
        return result;
    }

    public static <M extends EntityInterface, T extends DTO<M>> List<T> buildDtoListForCollection(List<M> collection, Class<T> dtoClass, Link collectionLink) {
        List<T> result = new ArrayList<>();
        if (collection == null) {
            return result;
        }
        for (M document : collection) {
            Link selfLink = new Link(collectionLink.getHref() + "/" + document.getId()).withSelfRel();
            T dtoForEntity = buildDtoForEntity(document, dtoClass, selfLink);
            result.add(dtoForEntity);
        }
        return result;
    }
}
