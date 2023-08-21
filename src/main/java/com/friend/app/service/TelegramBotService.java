package com.friend.app.service;

import com.friend.app.models.TelegramAccount;
import com.friend.app.models.person.Person;
import com.friend.app.setting.HibernateQualifier;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Slf4j
@Setter
@Component
public class TelegramBotService extends TelegramLongPollingBot {

    @Value("${telegram.username}")
    private String telegramUsername;
    @Value("${telegram.token}")
    private String telegramToken;

    private final MessageProducer messageProducer;
    private final PersonService personService;
    private final TelegramAccountService telegramAccountService;

    public TelegramBotService(MessageProducer messageProducer, @HibernateQualifier PersonService personService,
                              TelegramAccountService telegramAccountService) {
        this.messageProducer = messageProducer;
        this.personService = personService;
        this.telegramAccountService = telegramAccountService;
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
    public void onUpdateReceived(Update request) {
        if (!request.hasMessage())
            log.error("Message is null!");

        Message messageRequest = request.getMessage();
        String message= messageRequest.getText();

        if (message.equals("/start")) {
            setAnswerMessage(messageRequest, "Напишите username");
        } else if (message.startsWith("/username")) {
            int spaceIndex = message.indexOf(" ");
            if (spaceIndex != -1) {
                String username = message.substring(spaceIndex + 1);
                Optional<Person> person = personService.findByUsername(username);

                if (person.isEmpty()) {
                    setAnswerMessage(messageRequest, "Пользователя " + username + " нет в системе!");
                } else if (person.get().getTelegramAccount() != null) {
                    setAnswerMessage(messageRequest, "Синхронизация уже пройдена!");
                }
                else {
                    TelegramAccount account = new TelegramAccount(messageRequest.getChat().getUserName(),
                            messageRequest.getChatId());
                    telegramAccountService.create(account, person.get());
                    setAnswerMessage(messageRequest, "Синхронизация успешна!");
                }
            } else {
                setAnswerMessage(messageRequest, "Напиши через пробел: '/username имя_пользователя'");
            }

        } else if (message.startsWith("/remove")) {
            int spaceIndex = message.indexOf(" ");
            if (spaceIndex != -1) {
                String username = message.substring(spaceIndex + 1);
                Optional<Person> person = personService.findByUsername(username);

                if (person.isEmpty() || person.get().getTelegramAccount() == null) {
                    setAnswerMessage(messageRequest, "Пользователя " + username + " нет в системе!");
                } else {
                    telegramAccountService.remove(person.get());
                    setAnswerMessage(messageRequest, "Синхронизация удалена!");
                }
            } else {
                setAnswerMessage(messageRequest, "Напиши через пробел: '/remove имя_пользователя'");
            }
        }
        else {
            setAnswerMessage(messageRequest, "Погода сегодня замечательная!");
        }

        messageProducer.produce(messageRequest);
    }

    public void setAnswerMessage(Message messageRequest, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(messageRequest.getChatId().toString());
        sendMessage.setText(text);

        sendMessage(sendMessage);
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
