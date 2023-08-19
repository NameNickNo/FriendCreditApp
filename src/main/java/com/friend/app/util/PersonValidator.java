package com.friend.app.util;

import com.friend.app.models.person.Person;
import com.friend.app.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {

    private final PersonService personService;

    @Autowired
    public PersonValidator(@Qualifier("hibernatePersonService") PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        Optional<Person> byUsername = personService.findByUsername(person.getUsername());
        if (byUsername.isPresent())
            errors.rejectValue("username", "", "Имя: '" + person.getUsername() + "' уже занято!");
    }
}
