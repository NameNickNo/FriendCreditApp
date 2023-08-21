package com.friend.app.rabbit;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageConsumer {
    void consume(Message message);
}
