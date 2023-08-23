package com.friend.app.dto;

import com.friend.app.models.person.Person;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class PersonDTO {

    private long id;

    @NotEmpty(message = "can not be null!")
    @Size(min = 4, max = 255)
    private String fullName;

    @Size(min = 4, max = 50)
    @NotEmpty(message = "can not be null!")
    private String username;

    @Column(name = "birth_date")
    @NotNull(message = "can not be null!")
    private LocalDate birthDate;

    @Email(message = "email address is not valid!")
    private String email;

    public PersonDTO(Person person) {
        this.id = person.getId();
        this.fullName = person.getFullName();
        this.username = person.getUsername();
        this.birthDate = person.getBirthDate();
        this.email = person.getEmail();
    }
}
