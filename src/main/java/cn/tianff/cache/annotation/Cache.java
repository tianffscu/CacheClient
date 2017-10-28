package cn.tianff.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for Object cache in CacheSystem-Server
 * Created by Tianff on 2017/10/28.
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    /**
     * Default "" will make a packageName.ClassName as the cacheKey prefix
     * otherwise the prefix is packageName.value()
     */
    String value() default "";
}
