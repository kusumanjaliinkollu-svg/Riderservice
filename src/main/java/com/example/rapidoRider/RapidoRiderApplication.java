package com.example.rapidoRider;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RapidoRiderApplication {

	public static void main(String[] args) {
		SpringApplication.run(RapidoRiderApplication.class, args);
	}
	@Autowired
	private RedisConnectionFactory redisConnectionFactory;
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	@Bean
	public RedisTemplate<String, String> getRedisTemplate() {

	    RedisTemplate<String, String> redisTemplate =
	            new RedisTemplate<>();

	    redisTemplate.setConnectionFactory(redisConnectionFactory);

	    redisTemplate.setKeySerializer(new StringRedisSerializer());
	    redisTemplate.setValueSerializer(new StringRedisSerializer());

	    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
	    redisTemplate.setHashValueSerializer(new StringRedisSerializer());

	    redisTemplate.afterPropertiesSet();

	    return redisTemplate;
	}
	@Bean
	public RedisTemplate<String, Map<String,Object>> rideRedisTemplate() {

	    RedisTemplate<String, Map<String,Object>> template =
	            new RedisTemplate<>();

	    template.setConnectionFactory(redisConnectionFactory);

	    template.setKeySerializer(new StringRedisSerializer());
	    template.setValueSerializer(RedisSerializer.json());

	    template.afterPropertiesSet();

	    return template;
	}
	@Bean
	public RedisTemplate<String, String> lockRedisTemplate() {

	    RedisTemplate<String, String> template =
	            new RedisTemplate<>();

	    template.setConnectionFactory(redisConnectionFactory);

	    StringRedisSerializer serializer =
	            new StringRedisSerializer();

	    template.setKeySerializer(serializer);
	    template.setValueSerializer(serializer);
	    template.setHashKeySerializer(serializer);
	    template.setHashValueSerializer(serializer);

	    template.afterPropertiesSet();

	    return template;
	}
	@Bean
	public RedisTemplate<String, String> geoRedisTemplate() {

	    RedisTemplate<String, String> template =
	            new RedisTemplate<>();

	    template.setConnectionFactory(redisConnectionFactory);

	    StringRedisSerializer serializer =
	            new StringRedisSerializer();

	    template.setKeySerializer(serializer);
	    template.setValueSerializer(serializer);
	    template.setHashKeySerializer(serializer);
	    template.setHashValueSerializer(serializer);

	    template.afterPropertiesSet();

	    return template;
	}
	
	@Bean
	public RedisTemplate<String, String> bookingRedisTemplate() {

	    RedisTemplate<String, String> template = new RedisTemplate<>();

	    template.setConnectionFactory(redisConnectionFactory);

	    StringRedisSerializer serializer = new StringRedisSerializer();

	    template.setKeySerializer(serializer);
	    template.setValueSerializer(serializer);
	    template.setHashKeySerializer(serializer);
	    template.setHashValueSerializer(serializer);

	    template.afterPropertiesSet();

	    return template;
	}
}
