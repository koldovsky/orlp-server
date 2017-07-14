package com.softserve.academy.spaced.repetition.DTO;

import org.springframework.hateoas.Link;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DTOBuilder {

    public static <M extends EntityInterface, T extends DTO<M>> T buildDtoForEntity(M entity, Class<T> dtoClass, Link selfLink) throws  Exception{

        Constructor<T> declaredConstructor = dtoClass.getDeclaredConstructor(entity.getClass(), selfLink.getClass());

        T newInstance = declaredConstructor.newInstance(entity, selfLink);

        return newInstance;
    }

    public static <M extends EntityInterface, T extends DTO<M>> List<T> buildDtoListForCollection(List<M> collection, Class<T> dtoClass, Link collectionLink) throws Exception{
        List<T> result = new ArrayList<T>();
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
