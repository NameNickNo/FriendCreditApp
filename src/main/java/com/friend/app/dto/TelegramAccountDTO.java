package com.friend.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TelegramAccountDTO {

    private long id;

    private String username;

    private long chatId;

    private PersonDTO person;
}
