package com.friend.app.controllers;

import com.friend.app.dto.PersonChangePasswordDTO;
import com.friend.app.dto.PersonDTO;
import com.friend.app.models.person.Person;
import com.friend.app.security.PersonDetails;
import com.friend.app.service.PersonChangePasswordService;
import com.friend.app.service.PersonService;
import com.friend.app.setting.HibernateQualifier;
import com.friend.app.util.ErrorResponse;
import com.friend.app.util.exception.PersonChangePasswordException;
import com.friend.app.util.exception.PersonNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/person")
public class PersonRestController {

    private final PersonService personService;
    private final ModelMapper modelMapper;
    private final PersonChangePasswordService changePasswordService;

    public PersonRestController(@HibernateQualifier PersonService personService, ModelMapper modelMapper,
                                PersonChangePasswordService changePasswordService) {
        this.personService = personService;
        this.modelMapper = modelMapper;
        this.changePasswordService = changePasswordService;
    }

    @GetMapping
    public ResponseEntity<List<PersonDTO>> findAll() {
        List<PersonDTO> people = personService.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
        return people.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(people, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getOne(@PathVariable("id") long id) {
        Optional<Person> person = personService.findById(id);
        if (person.isEmpty())
            throw new PersonNotFoundException("Person with id = " + id + " not found!");

        return new ResponseEntity<>(convertToDTO(person.get()), HttpStatus.OK);
    }

    @PostMapping("/changepass")
    public ResponseEntity<HttpStatus> changePassword(@RequestBody @Valid PersonChangePasswordDTO changePasswordDTO,
                                                     BindingResult bindingResult,
                                                     @AuthenticationPrincipal PersonDetails personDetails) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            throw new PersonChangePasswordException("Attribute " + Objects.requireNonNull(fieldError).getField() + " "
                    + fieldError.getDefaultMessage());
        }

        changePasswordService.changePassword(personDetails.getPerson(), changePasswordDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PersonNotFoundException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PersonChangePasswordException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private PersonDTO convertToDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }
}
