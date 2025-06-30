package org.example.service;


import org.example.dto.UserDataTransferObject;
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
    static String USER_CACHE_PREFIX = "user:";

    public UserDataTransferObject getUserFromCache(Long id){
        String key = USER_CACHE_PREFIX + id;

        try(Jedis jedis = jedisPool.getResource()){
            String cashedJson = jedis.get(key);

            if(cashedJson != null){
                log.debug("User {} found in cache", id);
                return objectMapper.readValue(cashedJson, UserDataTransferObject.class);
            }

            log.debug("User {} not found in cache", id);
            return null;
        }catch(JsonProcessingException e){
            log.debug("Error deserialization user {} from cache", id, e);
            return null;
        }catch(Exception e){
            log.debug("Redis error when getting user {}", id, e);
            return null;
        }
    }

    public void cacheUser(Long userId, UserDataTransferObject userDataTransferObject){
        String key = USER_CACHE_PREFIX + userId;

        try(Jedis jedis = jedisPool.getResource()){
            String userJson = objectMapper.writeValueAsString(userDataTransferObject);
            jedis.setex(key, TTL, userJson);
            log.debug("User {} cashed successfully", userId);
        }
        catch(JsonProcessingException e){
            log.error("Error serialization user {} to cache", userId, e);
        }
        catch(Exception e){
            log.error("Error redis when cashing user {}", userId, e);
        }
    }

    public void evictUser(Long id){
        String key = USER_CACHE_PREFIX + id;

        try(Jedis jedis = jedisPool.getResource()){
            jedis.del(key);
            log.debug("User {} evicted from cache", id);
        }catch(Exception e){
            log.error("Redis error when evicting user {}", id,e);
        }
    }
    
    public void evictedAllUsers(){
        try(Jedis jedis = jedisPool.getResource()){
            String pattern = USER_CACHE_PREFIX + "*";
            var keys = jedis.keys(pattern);

            if(!keys.isEmpty()){
                jedis.del(keys.toArray(new String[0]));
                log.debug("Evicted {} users from cache", keys.size());
            }
        }
        catch(Exception e){
            log.error("Redis error when evicting all users", e);
        }
    }
    
}
