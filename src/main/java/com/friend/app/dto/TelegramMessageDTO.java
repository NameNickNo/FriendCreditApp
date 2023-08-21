package com.friend.app.dto;

import lombok.Data;

@Data
public class TelegramMessageDTO {

    private String to;
    private String text;

    public TelegramMessageDTO() {
    }

    public TelegramMessageDTO(String to, String text) {
        this.to = to;
        this.text = text;
    }
}
