package cn.tianff.cache;

/**
 * Base class of Cache
 * Created by Tianff on 2017/10/28.
 */
public abstract class CachedObject<E> {

    protected static final int CACHE_TYPE_HASH = 1;
    protected static final int CACHE_TYPE_SERIAL = 2;

    protected String cacheKey;

    protected transient E wrappedObj;

    protected transient int cacheType;

    protected CachedObject(String cacheKey, E wrappedObj) {
        this.cacheKey = cacheKey;
        this.wrappedObj = wrappedObj;
    }


    public String getCacheKey() {
        return cacheKey;
    }

    protected abstract void onCache();

    public CachedObject<E> cache() {

        onCache();

        return this;
    }

    protected abstract void onGet(Class<E> clazz);

    public E get(Class<E> clazz) {

        onGet(clazz);

        return wrappedObj;
    }

}