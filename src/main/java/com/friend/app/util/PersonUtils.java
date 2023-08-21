package com.friend.app.util;

import com.friend.app.models.person.Person;
import com.friend.app.security.PersonDetails;
import com.friend.app.service.PersonService;
import com.friend.app.setting.HibernateQualifier;
import com.friend.app.util.exception.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PersonUtils {

    private final PersonService personService;

    @Autowired
    public PersonUtils(@HibernateQualifier PersonService personService) {
        this.personService = personService;
    }

    public Person getCurrentPerson(PersonDetails personDetails) {
        Person authenticationPerson = personDetails.getPerson();
        Optional<Person> currentPerson = personService.findById(authenticationPerson.getId());
        if (currentPerson.isEmpty())
            throw new PersonNotFoundException("Error: current person with id " + authenticationPerson.getId() + " not found");
        return currentPerson.get();
    }
}
