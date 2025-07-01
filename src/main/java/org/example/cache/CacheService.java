package org.example.cache;


import java.util.Set;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@RequiredArgsConstructor
public class CacheService {

    JedisPool jedisPool;
    ObjectMapper objectMapper;

    static int TTL = 3600;

    public <T> T get(String key, Class<T> clazz){
        try(Jedis jedis = jedisPool.getResource()){
            String cashedJson = jedis.get(key);

            if(cashedJson != null){
                log.debug("Object found in cache by key: {}", key);
                return objectMapper.readValue(cashedJson, clazz);
            }

            log.debug("Object not found in cache by key: {}", key);
            return null;
        }catch(JsonProcessingException e){
            log.debug("Error deserialization object from cache, key: {}", key, e);
            return null;
        }catch(Exception e){
            log.debug("Redis error when getting object by key: {}", key, e);
            return null;
        }
    }

    public <T> void put(String key, T object, int ttlSeconds){
        try(Jedis jedis = jedisPool.getResource()){
            String objectJson = objectMapper.writeValueAsString(object);
            jedis.setex(key, ttlSeconds, objectJson);
            log.debug("Object cashed successfully with key: {}, TTL: {}", key, ttlSeconds);
        }
        catch(JsonProcessingException e){
            log.error("Error serialization object to cache, key: {}", key, e);
        }
        catch(Exception e){
            log.error("Error redis when cashing object, key: {}", key, e);
        }
    }

    public void evict(String key){
        try(Jedis jedis = jedisPool.getResource()){
            jedis.del(key);
            log.debug("Object evicted from cache, key: {}", key);
        }catch(Exception e){
            log.error("Redis error when evicting object, key: {}", key,e);
        }
    }
    
    public void evictedByPattern(String pattern){
        try(Jedis jedis = jedisPool.getResource()){
            Set<String> keys = jedis.keys(pattern);
            if(!keys.isEmpty()){
                jedis.del(keys.toArray(new String[0]));
                log.debug("Evicted {} objects from cache by pattern: {}", keys.size());
            }
        }
        catch(Exception e){
            log.error("Redis error when evicting objects by pattern: {}", pattern, e);
        }
    }

    public boolean exists(String key){
        try(Jedis jedis = jedisPool.getResource()){
            return jedis.exists(key);
        }
        catch(Exception e){
            log.error("Redis error when checking key existence: {}", key, e);
            return false;
        }
    }
    
    public void expire(String key, int ttlSeconds){
        try(Jedis jedis = jedisPool.getResource()){
            jedis.expire(key, ttlSeconds);
            log.debug("TTL set for key: {}, TTL: {}", key, ttlSeconds);
        }
        catch(Exception e){
            log.error("Redis error when setting TTL for key: {}", key, e);
        }
    }
}
