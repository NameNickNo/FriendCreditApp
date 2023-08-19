package com.friend.app.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    public static final String TEXT_MESSAGE_QUEUE = "text_message_queue";

//    @Bean
//    public Queue textMessageQueue() {
//        return new Queue(TEXT_MESSAGE_QUEUE);
//    }
}
