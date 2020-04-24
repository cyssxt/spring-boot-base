package com.cyssxt.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by zqy on 18/05/2018.
 */
@Component
public class RedisUtil {
    private final static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public  <T> void objectSetWithExpireTime(String key, T t, int time,TimeUnit unit){
        ValueOperations<String, T> ops = redisTemplate.opsForValue();
        ops.set(key,t,time,unit);
        logger.debug("key={},value={}",key,t);
    }

    public  <T> void objectSet(String key, T t){
        ValueOperations<String, T> ops = redisTemplate.opsForValue();
        ops.set(key,t);
        logger.debug("key={},value={}",key,t);
    }

    public Boolean delKey(String key){
        return redisTemplate.delete(key);
    }


    public <T>T getObjectValue(String key){
        ValueOperations<String, T> ops = this.redisTemplate.opsForValue();
        T t = ops.get(key);
        return t;
    }

    public  void stringSetWithExpireTime(String key,String value,int timeout,TimeUnit timeUnit){
        ValueOperations<String, String> ops = this.stringRedisTemplate.opsForValue();
        ops.set(key,value,timeout,timeUnit);
    }
    public void stringSet(String key,String value,int timeout,TimeUnit timeUnit){
        ValueOperations<String, String> ops = this.stringRedisTemplate.opsForValue();
        ops.set(key,value,timeout,timeUnit);
    }
    public void stringSet(String key,String value){
        ValueOperations<String, String> ops = this.stringRedisTemplate.opsForValue();
        ops.set(key,value);
    }
    public String getStringValue(String key){
        ValueOperations<String, String> ops = this.stringRedisTemplate.opsForValue();
        return ops.get(key);
    }

    public <T>void sadd(String key,T ...t){
        SetOperations<String,T> ops = redisTemplate.opsForSet();
        ops.add(key,t);
    }

    public <T> T spop(String key){
        SetOperations<String,T> ops = redisTemplate.opsForSet();
        return ops.pop(key);
    }

    public void saddStr(String key,String ...value){
        SetOperations<String,String> ops = this.stringRedisTemplate.opsForSet();
        ops.add(key,value);
    }

    public String spopStr(String key){
        SetOperations<String,String> ops = this.stringRedisTemplate.opsForSet();
        return ops.pop(key);
    }

    public <T> void hset(String key,String hashKey,T value){
        HashOperations<String,String,T> hashOperations = this.stringRedisTemplate.opsForHash();
        hashOperations.put(key,hashKey,value);
    }

    public <T> T hget(String key,String hashKey){
        HashOperations<String,String,T> hashOperations = this.stringRedisTemplate.opsForHash();
        return hashOperations.get(key,hashKey);
    }

    public <T> Long hdel(String key,String hashKey){
        HashOperations<String,String,T> hashOperations = this.stringRedisTemplate.opsForHash();
        return hashOperations.delete(key,hashKey);
    }

    public <T> Set<String> hKeys(String key){
        HashOperations<String,String,T> hashOperations = this.stringRedisTemplate.opsForHash();
        return hashOperations.keys(key);
    }

    public boolean clear(String key){
        return this.stringRedisTemplate.delete(key);
    }


}
