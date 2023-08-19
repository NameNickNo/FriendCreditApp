package com.friend.app.service;

import com.friend.app.util.MessageUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Locale;

@Slf4j
@Setter
@Component
public class TelegramBotService extends TelegramLongPollingBot {

    @Value("${telegram.username}")
    private String telegramUsername;
    @Value("${telegram.token}")
    private String telegramToken;

    private final MessageProducer messageProducer;

    public TelegramBotService(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    @Override
    public String getBotUsername() {
        return telegramUsername;
    }

    @Override
    public String getBotToken() {
        return telegramToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage())
            log.error("Message is null!");

        Message message = update.getMessage();
        String messageText = message.getText();
        log.info(messageText);

        messageProducer.produce(message);
        sendMessage(MessageUtils.generateSendMessageWithText(message, messageText.toUpperCase(Locale.ROOT)));
    }

    public void sendMessage(SendMessage message) {
        if (message != null) {
            try {
                super.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
