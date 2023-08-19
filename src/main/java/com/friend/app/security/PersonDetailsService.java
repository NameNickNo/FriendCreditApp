package com.friend.app.security;

import com.friend.app.models.person.Person;
import com.friend.app.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PersonService personService;

    @Autowired
    public PersonDetailsService(@Qualifier("hibernatePersonService") PersonService personService) {
        this.personService = personService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personService.findByUsername(username);
        if (person.isEmpty()) {
            throw new UsernameNotFoundException("Person with username: " + username + " not found!");
        }
        return new PersonDetails(person.get());
    }

    public Person getAuthenticationPerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return personDetails.getPerson();
    }
}
