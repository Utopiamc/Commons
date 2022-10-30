package de.utopiamc.commons.reflection.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeToken<T> {

    private final Type type;

    protected TypeToken() {
        this.type = captureType();
    }

    private Type captureType() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = ((ParameterizedType) genericSuperclass);
            if (parameterizedType.getRawType().equals(TypeToken.class)) {
                return parameterizedType.getActualTypeArguments()[0];
            }
        }

        else if (genericSuperclass == TypeToken.class) {
            throw new IllegalStateException("TypeToken must be created with a type argument: new TypeToken<...>() {}; "
                    + "When using code shrinkers (ProGuard, R8, ...) make sure that generic signatures are preserved.");
        }
        throw new IllegalStateException("Must only create direct subclasses of TypeToken");
    }

    public Type getType() {
        return type;
    }
}
