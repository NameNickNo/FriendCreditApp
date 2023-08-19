package com.friend.app.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PersonDTO {

    private long id;

    @NotEmpty(message = "Обязателен к заполнению!")
    @Size(min = 4, max = 255)
    private String fullName;

    @Size(min = 4, max = 50)
    @NotEmpty(message = "Обязателен к заполнению!")
    private String username;

    @Column(name = "birth_date")
    @NotNull(message = "Обязателен к заполнению!")
    private LocalDate birthDate;

    @Email(message = "Адрес электронной почты должен быть валидным")
    private String email;

    private List<FriendshipDTO> friends;

}
