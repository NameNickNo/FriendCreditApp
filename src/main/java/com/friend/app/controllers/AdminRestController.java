package com.friend.app.controllers;

import com.friend.app.dto.PersonDTO;
import com.friend.app.models.person.Person;
import com.friend.app.service.PersonService;
import com.friend.app.setting.HibernateQualifier;
import com.friend.app.util.ErrorResponse;
import com.friend.app.util.exception.PersonNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    private final PersonService personService;

    public AdminRestController(@HibernateQualifier PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/person/edit")
    public ResponseEntity<HttpStatus> editPerson(@RequestBody PersonDTO personDTO) {
        Optional<Person> person = personService.findById(personDTO.getId());
        if (person.isEmpty())
            throw new PersonNotFoundException("Person with id = " + personDTO.getId() + " not found!");

        personService.update(personDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/person/remove/{id}")
    public ResponseEntity<HttpStatus> removePerson(@PathVariable("id") long id) {
        Optional<Person> person = personService.findById(id);
        if (person.isEmpty())
            throw new PersonNotFoundException("Person with id = " + id + " not found!");

        personService.remove(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PersonNotFoundException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
