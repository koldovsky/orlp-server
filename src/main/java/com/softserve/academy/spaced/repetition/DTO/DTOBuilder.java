package com.softserve.academy.spaced.repetition.DTO;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class DTOBuilder {

    public static <M extends EntityInterface, T extends DTO<M>> T buildDtoForEntity(M entity, Class<T> dtoClass) throws Exception {
        Constructor<T> declaredConstructor = dtoClass.getDeclaredConstructor(entity.getClass());
        T newInstance = declaredConstructor.newInstance(entity);
        return newInstance;
    }

    public static <M extends EntityInterface, T extends DTO<M>> List<T> buildDtoListForCollection(List<M> collection, Class<T> dtoClass) throws Exception {
        List<T> result = new ArrayList<T>();
        if (collection == null) {
            return result;
        }
        for (M document : collection) {
            T dtoForEntity = buildDtoForEntity(document, dtoClass);
            result.add(dtoForEntity);
        }
        return result;
    }
}
