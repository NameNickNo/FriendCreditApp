package com.friend.app.service.impl;

import com.friend.app.models.person.Person;
import com.friend.app.repo.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaPersonServiceTest {

    @InjectMocks
    private JpaPersonService personService;

    @Mock
    private PersonRepository personRepository;

    private final List<Person> persons = new ArrayList<>(List.of(
            new Person("Test name", "Test@email.com", "Test data"),
            new Person("Test name 2", "Test@email.com", "Test data 2")
    ));

    private final Optional<Person> person = Optional.of(persons.get(0));

    @Test
    void findAllShouldWork() {
        when(personRepository.findAll()).thenReturn(persons);
        List<Person> personListTest = personService.findAll();

        assertEquals(2, personListTest.size());
        verify(personRepository, times(1)).findAll();
    }

    @Test
    void findByIdShouldWork() {
        long id = 1;
        when(personRepository.findById(id)).thenReturn(person);
        Optional<Person> testPerson = personService.findById(id);

        assertNotEquals(Optional.empty(), testPerson);
        assertNotNull(testPerson);
        verify(personRepository, times(1)).findById(id);
    }
}