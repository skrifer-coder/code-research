package skrifer.github.com.code.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericClass<T> {
    private Class<T> genericType;

    public GenericClass() {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        this.genericType = (Class<T>) actualTypeArguments[0];
    }

    public Class<T> getGenericType() {
        return genericType;
    }

    public static void main(String[] args) {
        GenericClass<String> genericClass = new GenericClass<String>(){};

        Class<String> genericType = genericClass.getGenericType();
        System.out.println(genericType);
    }
}
