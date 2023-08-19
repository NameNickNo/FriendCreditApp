package com.friend.app.controllers;

import com.friend.app.dto.PersonDTO;
import com.friend.app.models.BaseEntity;
import com.friend.app.models.Friendship;
import com.friend.app.models.person.Person;
import com.friend.app.service.PersonService;
import com.friend.app.setting.HibernateQualifier;
import com.friend.app.util.ErrorResponse;
import com.friend.app.util.exception.PersonNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/person")
public class PersonRestController {

    private final PersonService personService;
    private final ModelMapper modelMapper;

    public PersonRestController(@HibernateQualifier PersonService personService, ModelMapper modelMapper) {
        this.personService = personService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<PersonDTO> findAll() {
        return personService.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PersonDTO getOne(@PathVariable("id") long id) {
        Person person = personService.findById(id).orElse(null);
        if (person == null)
            throw new PersonNotFoundException("Person with id = " + id + " not found!");

        return convertToDTO(person);
    }

    @PostMapping("/edit/{id}")
    public PersonDTO editPerson(@RequestBody PersonDTO personDTO) {
//        Person person = personService.findById(id).orElse(null);
//        if (person == null)
//            throw new PersonNotFoundException("Person with id = " + id + " not found!");
//        personService.update(person);

        return null;
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PersonNotFoundException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    private PersonDTO convertToDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }
}
