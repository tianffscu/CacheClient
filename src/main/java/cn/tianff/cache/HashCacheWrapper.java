package cn.tianff.cache;


import cn.tianff.util.ClassUtil;

import java.util.Map;

/**
 * Using Redis Hash structure to save object
 * Using HashMap to hold content object
 * Created by Tianff on 2017/10/28.
 */
public class HashCacheWrapper<E> extends CachedObject<E> {

    private Map<String, Object> contentFieldMap;

    public HashCacheWrapper(String cacheKey, E wrappedObj) {
        super(cacheKey, wrappedObj);

        this.cacheType = CACHE_TYPE_HASH;
    }

    @Override
    protected void onCache() {

        this.contentFieldMap = ClassUtil.getFieldMap(wrappedObj);

    }

    @Override
    protected void onGet(Class<E> clazz) {

        this.wrappedObj = ClassUtil.getObjectFromFieldMap(contentFieldMap, clazz);

    }
}

