package cn.tianff.ops;

import cn.tianff.cache.CachedObject;
import cn.tianff.cache.HashCacheWrapper;
import cn.tianff.cache.annotation.Cache;
import cn.tianff.cache.annotation.Key;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Using as a DAO CRUD to simply operate the cache
 * Created by Tianff on 2017/10/28.
 */
public class CacheTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheTemplate.class);

    private static final String PROTOCOL = "http://";
    private static final String DEFAULT_DOMAIN_NAME = "localhost:8080";

    private static final String DEFAULT_CRUD_REST_URL = "/cache/api/object";

    private Gson defaultJsonParser = new Gson();

    private String domainName;
    private String retrieveUrl;
    private String saveUrl;
    private String deleteUrl;

    private String requestBaseUrl;

    private RestTemplate restTemplate;

    public CacheTemplate() {
        this(DEFAULT_DOMAIN_NAME, DEFAULT_CRUD_REST_URL, DEFAULT_CRUD_REST_URL, DEFAULT_CRUD_REST_URL);
    }

    public CacheTemplate(String domainName, String retrieveUrl, String saveUrl, String deleteUrl) {
        this.retrieveUrl = retrieveUrl;
        this.saveUrl = saveUrl;
        this.deleteUrl = deleteUrl;
        this.domainName = domainName;

        this.requestBaseUrl = PROTOCOL + domainName;

        this.restTemplate = new RestTemplate();
        //exclude the jackson json, only using google gson
        this.restTemplate.getMessageConverters().removeIf(conv -> conv instanceof MappingJackson2HttpMessageConverter);
    }

    public <T> T retrieve(String key, Class<T> clazz) {
        String url = requestBaseUrl + retrieveUrl + getCacheKeyPrefix(clazz) + ":" + "key";
//        This line may be not safe
//        CachedObject<T> cachedObject = restTemplate.getForObject(url, HashCacheWrapper.class);
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
        CachedObject<T> cachedObject = defaultJsonParser.fromJson(entity.getBody(), new TypeToken<HashCacheWrapper<T>>() {
        }.getType());

        return cachedObject.get(clazz);
    }

    public void save(Object obj) {

        String cacheKey = getCacheKey(obj);
        String url = requestBaseUrl + saveUrl + cacheKey;

        CachedObject<?> cachedObj = new HashCacheWrapper<>(cacheKey, obj);
        String resp = restTemplate.postForObject(url, cachedObj.cache(), String.class);

        LOGGER.debug("Save result: " + resp);
    }

    public void remove(Object obj) {
        String cacheKey = getCacheKey(obj);
        String url = requestBaseUrl + deleteUrl + cacheKey;

        restTemplate.delete(url);
    }

    public void remove(String key, Class<?> clazz) {
        String cacheKeyPrefix = getCacheKeyPrefix(clazz);
        String url = requestBaseUrl + deleteUrl + cacheKeyPrefix + ":" + key;

        restTemplate.delete(url);
    }


    private String getCacheKeyPrefix(Class<?> clazz) {

        Cache anno = clazz.getAnnotation(Cache.class);
        if (anno == null) {
            throw new IllegalStateException("Check if your class " + clazz.getName() + " has annotation \"cn.tianff.cache.annotation.Cache\"!");
        }

        return clazz.getPackage() + "." + ("".equals(anno.value()) ? clazz.getName() : anno.value());
    }

    private String getCacheKey(Object obj) {

        String keyPrefix = getCacheKeyPrefix(obj.getClass());

        List<Field> fields = Arrays.stream(obj.getClass().getDeclaredFields()).filter(field -> field.getAnnotation(Key.class) != null).collect(Collectors.toList());

        if (fields.size() != 1) {
            throw new IllegalStateException("Your object class must have one and only one annotation \"cn.tianff.cache.annotation.Key\" on field to indicate a reasonable key!");
        }

        try {
            Field f = fields.get(0);
            f.setAccessible(true);
            return keyPrefix + ":" + f.get(obj).toString();
        } catch (IllegalAccessException e) {
            LOGGER.error("Unexpected error: " + e);
            return null;
        }
    }
}
