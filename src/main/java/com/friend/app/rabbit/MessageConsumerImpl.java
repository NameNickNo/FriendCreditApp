package com.friend.app.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@Slf4j
@EnableRabbit
public class MessageConsumerImpl implements MessageConsumer {

    @Override
    @RabbitListener(queues = RabbitQueue.TEXT_MESSAGE_QUEUE)
    public void consume(Message message) {
        log.info(message.getText(), "Message getting {}");
    }
}
