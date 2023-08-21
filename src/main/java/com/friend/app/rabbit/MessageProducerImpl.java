package com.friend.app.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class MessageProducerImpl implements MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageProducerImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(Message message) {
        rabbitTemplate.convertAndSend(RabbitQueue.TEXT_MESSAGE_QUEUE, message);
    }
}
