package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
    
    @Value("${redis.host:redis}")
    private String redisHost;
    
    @Value("${redis.port:6379}")
    private int redisPort;
    
    @Value("${redis.password:}")
    private String redisPassword;
    
    @Value("${redis.database:0}")
    private int redisDatabase;
    
    @Value("${redis.pool.max-total:20}")
    private int maxTotal;
    
    @Value("${redis.pool.max-idle:10}")
    private int maxIdle;
    
    @Value("${redis.pool.min-idle:5}")
    private int minIdle;
    
    @Value("${redis.pool.max-wait-millis:2000}")
    private long maxWaitMillis;
    
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        config.setTestWhileIdle(true);
        return config;
    }
    
    @Bean
    public JedisPool jedisPool(JedisPoolConfig jedisPoolConfig) {
        if (redisPassword != null && !redisPassword.isEmpty()) {
            return new JedisPool(jedisPoolConfig, redisHost, redisPort, 2000, redisPassword, redisDatabase);
        } else {
            return new JedisPool(jedisPoolConfig, redisHost, redisPort, 2000, null, redisDatabase);
        }
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}