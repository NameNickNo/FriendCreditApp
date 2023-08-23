package com.friend.app.service.impl;

import com.friend.app.dto.PersonDTO;
import com.friend.app.models.person.Person;
import com.friend.app.repo.PersonRepository;
import com.friend.app.service.PersonService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JpaPersonService implements PersonService {

    private final PersonRepository personRepository;

    public JpaPersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public Optional<Person> findById(long id) {
        return personRepository.findById(id);
    }

    @Override
    public Optional<Person> findByUsername(String username) {
        return personRepository.findByUsername(username);
    }

    @Override
    public Optional<Person> findByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    @Override
    public void save(Person person) {
        personRepository.save(person);
    }

    @Override
    public void update(PersonDTO person) {

    }

    @Override
    public void remove(long id) {
        personRepository.deleteById(id);
    }

    @Override
    public List<Person> findOtherPersonsWithoutFriends(Person currentPerson) {
        return new ArrayList<>();
    }
}
