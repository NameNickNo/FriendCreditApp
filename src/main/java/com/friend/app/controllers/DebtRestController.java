package com.friend.app.controllers;

import com.friend.app.dto.DebtDTO;
import com.friend.app.models.Debt;
import com.friend.app.service.DebtService;
import com.friend.app.setting.HibernateQualifier;
import com.friend.app.util.ErrorResponse;
import com.friend.app.util.exception.DebtCreationException;
import com.friend.app.util.exception.DebtNotFoundException;
import com.friend.app.util.exception.PersonNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/debt")
public class DebtRestController {

    private final DebtService debtService;
    private final ModelMapper modelMapper;

    public DebtRestController(@HibernateQualifier DebtService debtService, ModelMapper modelMapper) {
        this.debtService = debtService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<DebtDTO>> getDebts() {
        List<DebtDTO> people = debtService.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(people);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DebtDTO> getDebtById(@PathVariable("id") int id) {
        Optional<Debt> debt = debtService.findById(id);
        if (debt.isEmpty())
            throw new DebtNotFoundException("Debt with id " + id + " not found!");

        return ResponseEntity.ok(convertToDto(debt.get()));
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> createDebt(@RequestBody @Valid DebtDTO debtDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new DebtCreationException("Creation error");

        debtService.create(debtDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(DebtNotFoundException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(DebtCreationException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PersonNotFoundException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private DebtDTO convertToDto(Debt debt) {
        return modelMapper.map(debt, DebtDTO.class);
    }
}
