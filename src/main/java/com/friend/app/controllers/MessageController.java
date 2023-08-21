package com.friend.app.controllers;

import com.friend.app.dto.TelegramMessageDTO;
import com.friend.app.models.person.Person;
import com.friend.app.security.PersonDetails;
import com.friend.app.service.PersonService;
import com.friend.app.service.TelegramBotService;
import com.friend.app.setting.HibernateQualifier;
import com.friend.app.util.ErrorResponse;
import com.friend.app.util.PersonUtils;
import com.friend.app.util.exception.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final TelegramBotService telegramBotService;
    private final PersonUtils personUtils;
    private final PersonService personService;

    @Autowired
    public MessageController(TelegramBotService telegramBotService, PersonUtils personUtils,
                             @HibernateQualifier PersonService personService) {
        this.telegramBotService = telegramBotService;
        this.personUtils = personUtils;
        this.personService = personService;
    }

    @PostMapping("/telegram")
    public ResponseEntity<HttpStatus> sendTelegramNotification(@AuthenticationPrincipal PersonDetails personDetails,
                                                               @RequestBody TelegramMessageDTO telegramMessageDTO) {
        Person currentPerson = personUtils.getCurrentPerson(personDetails);
        Optional<Person> personTo = personService.findByUsername(telegramMessageDTO.getTo());
        if (personTo.isEmpty())
            throw new PersonNotFoundException("Person with username " + telegramMessageDTO.getTo() + " not found!");

        String customText = "Пользователь " + currentPerson.getUsername() + " просит вернуть долг. \n"
                + telegramMessageDTO.getText();

        SendMessage message = new SendMessage();
        message.setText(customText);
        message.setChatId(personTo.get().getTelegramAccount().getChatId());
        telegramBotService.sendMessage(message);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PersonNotFoundException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
