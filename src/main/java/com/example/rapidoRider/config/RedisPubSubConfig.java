package com.example.rapidoRider.config;

	import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.data.redis.connection.RedisConnectionFactory;
	import org.springframework.data.redis.listener.ChannelTopic;
	import org.springframework.data.redis.listener.RedisMessageListenerContainer;
	import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
    import com.example.rapidoRider.Services.RideRequestListener;

    @Configuration
    public class RedisPubSubConfig {

        @Value("${rider.id}")
        private int riderId;

        @Value("${rider.vehicle}")
        private String vehicleType;

        @Bean
        public MessageListenerAdapter messageListenerAdapter(
                RideRequestListener listener) {

            return new MessageListenerAdapter(listener, "receiveMessage");
        }

        @Bean
        public RedisMessageListenerContainer redisMessageListenerContainer(
                RedisConnectionFactory connectionFactory,
                MessageListenerAdapter messageListenerAdapter) {

            RedisMessageListenerContainer container =
                    new RedisMessageListenerContainer();

            container.setConnectionFactory(connectionFactory);

            String channel = "ride-request:"
                    + vehicleType.toLowerCase()
                    + ":"
                    + riderId;

            System.out.println("Rider subscribing to: " + channel);

            container.addMessageListener(
                    messageListenerAdapter,
                    new ChannelTopic(channel)
            );

            return container;
        }
	}

