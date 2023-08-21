package com.friend.app.service;

import com.friend.app.models.TelegramAccount;
import com.friend.app.models.person.Person;


public interface TelegramAccountService {

    void create(TelegramAccount telegramAccount, Person person);

    void remove(Person person);
}
