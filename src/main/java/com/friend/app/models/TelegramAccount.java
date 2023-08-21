package com.friend.app.models;

import com.friend.app.models.person.Person;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "telegram_account")
@Data
public class TelegramAccount extends BaseEntity{

    @Column(name = "username")
    private String username;

    @Column(name = "chat_id")
    private long chatId;

    @OneToOne(mappedBy = "telegramAccount")
    private Person person;

    public TelegramAccount() {
    }

    public TelegramAccount(String username, long chatId) {
        this.username = username;
        this.chatId = chatId;
    }
}
