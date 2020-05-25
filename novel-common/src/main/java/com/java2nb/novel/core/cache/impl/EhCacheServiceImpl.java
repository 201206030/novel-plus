package com.java2nb.novel.core.cache.impl;

import com.java2nb.novel.core.cache.CacheService;
import lombok.RequiredArgsConstructor;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @author xxy
 */
@ConditionalOnProperty(prefix = "cache", name = "type", havingValue = "ehcache")
@RequiredArgsConstructor
@Service
public class EhCacheServiceImpl implements CacheService {
	
	private final CacheManager cacheManager ;
 

	/**
	 * 获得一个Cache，没有则创建一个。
	 * @return
	 */
	private Cache getCache(){
	
		Cache cache = cacheManager.getCache("util_cache");
		return cache;
	}
	
 
	@Override
	public String get(String key) {
		Element element = getCache().get(key);
		return element==null?null:(String)element.getObjectValue();
	}

	@Override
	public void set(String key, String value) {
		Element element = new Element(key, value);
		Cache cache = getCache();
		//不过期
		cache.getCacheConfiguration().setEternal(true);
		cache.put(element);

	}

	@Override
	public void set(String key, String value, long timeout) {
		Element element = new Element(key, value);
		element.setTimeToLive((int) timeout);
		Cache cache = getCache();
		cache.put(element);

	}

	@Override
	public void del(String key) {
		getCache().remove(key);


	}

	@Override
	public boolean contains(String key) {
		return getCache().isKeyInCache(key);
	}

	@Override
	public void expire(String key, long timeout) {
		Element element = getCache().get(key);
		if (element != null) {
			Object value = element.getValue();
			element = new Element(key, value);
			element.setTimeToLive((int)timeout);
			Cache cache = getCache();
			cache.put(element);
		}
	}


	/**
	 * 根据key获取缓存的Object类型数据
	 */
	@Override
	public Object getObject(String key) {
		Element element = getCache().get(key);
		return element==null?null:element.getObjectValue();
	}


	/**
	 * 设置Object类型的缓存
	 */
	@Override
	public void setObject(String key, Object value) {
		Element element = new Element(key, value);
		Cache cache = getCache();
		//不过期
		cache.getCacheConfiguration().setEternal(true);
		cache.put(element);
		
	}


	/**
	 * 设置一个有过期时间的Object类型的缓存,单位秒
	 */
	@Override
	public void setObject(String key, Object value, long timeout) {
		Element element = new Element(key, value);
		element.setTimeToLive((int) timeout);
		Cache cache = getCache();
		cache.put(element);
		
	}




}
