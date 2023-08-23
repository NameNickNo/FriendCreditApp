package com.friend.app.service;

import com.friend.app.dto.PersonDTO;
import com.friend.app.models.person.Person;
import com.friend.app.dto.PersonChangePasswordDTO;

import java.util.List;
import java.util.Optional;

public interface PersonService {

     List<Person> findAll();

     Optional<Person> findById(long id);

     Optional<Person> findByUsername(String username);

     Optional<Person> findByEmail(String email);

     void save(Person person);

     void update(PersonDTO person);

     void remove(long id);

     List<Person> findOtherPersonsWithoutFriends(Person currentPerson);
}
