package com.friend.app.service.impl;

import com.friend.app.config.RabbitConfiguration;
import com.friend.app.service.MessageProducer;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@EnableRabbit
public class MessageProducerImpl implements MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageProducerImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    @RabbitListener(queues = "text_message_queue")
    public void produce(Message message) {
        rabbitTemplate.convertAndSend(RabbitConfiguration.TEXT_MESSAGE_QUEUE, message);
    }
}
