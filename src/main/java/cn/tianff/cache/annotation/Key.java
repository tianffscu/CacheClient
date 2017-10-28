package cn.tianff.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for object cache in CacheSystem-Server
 * Annotated field value.toString() will be call as the cacheKey suffix
 * Created by Tianff on 2017/10/28.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Key {
}
