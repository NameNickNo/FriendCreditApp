package com.friend.app.service;

import com.friend.app.models.TelegramAccount;
import com.friend.app.models.person.Person;

import java.util.List;


public interface TelegramAccountService {

    List<TelegramAccount> findAll();

    void create(TelegramAccount telegramAccount, Person person);

    void remove(Person person);
}
