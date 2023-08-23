package com.friend.app.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PersonChangePasswordDTO {

    @Size(min = 4, max = 50, message = "must be more than 4 and less than 50 symbols!")
    @NotEmpty(message = "can not be null!")
    private String oldPassword;

    @Size(min = 4, max = 50, message = "must be more than 4 and less than 50 symbols!")
    @NotEmpty(message = "can not be null!")
    private String newPassword;

    @Size(min = 4, max = 50, message = "must be more than 4 and less than 50 symbols!")
    @NotEmpty(message = "can not be null!")
    private String repeatNewPassword;

}
