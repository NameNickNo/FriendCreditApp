package com.friend.app.service;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageProducer {

    void produce(Message message);
}
