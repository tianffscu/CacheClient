package cn.tianff.ops;

import cn.tianff.cache.annotation.Cache;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Using as a DAO CRUD to simply operate the cache
 * Created by Tianff on 2017/10/28.
 */
public class CacheTemplate {

    private RestTemplate restTemplate;

    public CacheTemplate() {

        this.restTemplate = new RestTemplate();
        //exclude the jackson json, only using google gson

        this.restTemplate.getMessageConverters().removeIf(conv -> conv instanceof MappingJackson2HttpMessageConverter);

    }


    public <T> T retrieve(String key, Class<T> clazz) {

        return null;
    }

    public void save(Object obj) {

    }

    public void remove(Object obj) {

    }

    public void remove(String key) {

    }

    private String getCacheKeyPrefix(Class<?> clazz) {

        Cache anno = clazz.getAnnotation(Cache.class);
        if (anno == null) {
            throw new IllegalStateException("Check if your class " + clazz.getName() + " has annotation \"cn.tianff.cache.annotation.Cache\"!");
        }


        return null;
    }

    private String getCacheKey(Object obj) {

        return null;
    }
}
