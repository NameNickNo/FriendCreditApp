package com.friend.app.models.person;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PersonChangePasswordEntity {

    @Size(min = 3, max = 50)
    @NotEmpty(message = "Обязателен к заполнению")
    private String oldPassword;

    @Size(min = 4, max = 50)
    @NotEmpty(message = "Обязателен к заполнению")
    private String newPassword;

    @Size(min = 4, max = 50)
    @NotEmpty(message = "Обязателен к заполнению")
    private String repeatNewPassword;

}
