package com.springredis.redispractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.springredis.redispractice.Pojos.Receiver;

@Configuration
@ComponentScan(basePackages = {"com.springredis"})
@EnableWebMvc
public class AppConfig {

    /*This container works for setting the redis connection properties
     * from our application.properties, as well as initializing the message listener,
     * setting the pattern topic (the name of the channel).
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("chat"));

        return container;
    }

    /*This is our message listener, so we pass the instance of our bean, 
    in this case our pojo, and as second parameter you will indicate a method inside
    the pojo that is going to process the received message, that method must have
    a parameter of type string to get the message. In our case the method 
    is receiveMessage(String message):void
     */
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    /*This is our pojo class to retrieve the messages from the listener.
     * You can aso set a flag for message limit I think.
    */
    @Bean
    Receiver receiver() {
        return new Receiver();
    }

    /*This class works for sending the messages to a topic chat, along with the 
     * message content. That is made from the main class. It's like
     * a pub sub pattern.
     */
    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}
