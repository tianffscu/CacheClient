package cn.tianff.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Refection methods for class operations
 * Created by Tianff on 2017/10/28.
 */
public class ClassUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    private ClassUtil() {
    }

    /**
     * Generate a Map which hold every field of the given object
     *
     * @param obj object
     * @return a map
     */
    public static Map<String, Object> getFieldMap(Object obj) {

        Field[] fields = obj.getClass().getDeclaredFields();

        return Arrays.stream(fields).collect(Collectors.toMap(
                Field::getName, f -> {

                    f.setAccessible(true);
                    try {
                        return f.get(obj);
                    } catch (IllegalAccessException e) {
                        LOGGER.error("Error in field.get() " + e);
                        return null;
                    }
                }
        ));
    }

    /**
     * Recover a object from Map generated from @link{getFieldMap}
     *
     * @param map   Map generated from @link{getFieldMap}
     * @param clazz Specific Class
     * @param <T>   Type
     * @return T object
     */
    public static <T> T getObjectFromFieldMap(Map<String, Object> map, Class<T> clazz) {

        Field[] fields = clazz.getDeclaredFields();

        try {
            T instance = clazz.newInstance();
            Arrays.stream(fields).forEach(field -> {
                Object value = map.get(field.getName());

                if (value != null) {
                    field.setAccessible(true);
                    try {
                        field.set(instance, value);
                    } catch (IllegalAccessException e) {
                        //this may not happen!
                        LOGGER.error("Unexpected!" + e);
                        throw new IllegalStateException("Unexpected exception: " + e);
                    }
                }
            });
            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("No default constructor!: " + e);
            throw new IllegalStateException("Check if your class " + clazz.getName() + " has a default constructor");
        }
    }
}