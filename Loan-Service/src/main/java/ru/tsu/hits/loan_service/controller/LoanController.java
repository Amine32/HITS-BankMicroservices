package ru.tsu.hits.loan_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.loan_service.dto.LoanDto;
import ru.tsu.hits.loan_service.dto.converter.LoanDtoConverter;
import ru.tsu.hits.loan_service.service.LoanService;

import java.util.Optional;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;
    private final LoanDtoConverter loanDtoConverter;

    @Autowired
    public LoanController(LoanService loanService, LoanDtoConverter loanDtoConverter) {
        this.loanService = loanService;
        this.loanDtoConverter = loanDtoConverter;
    }

    @PostMapping
    public ResponseEntity<LoanDto> createLoan(@RequestBody LoanDto loanDto) {
        return ResponseEntity.ok(loanDtoConverter.convertToDto(loanService.createLoan(loanDtoConverter.convertToEntity(loanDto))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDto> getLoan(@PathVariable Long id) {
        Optional<LoanDto> loanDto = loanService.getLoan(id).map(loanDtoConverter::convertToDto);
        return loanDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanDto> updateLoan(@PathVariable Long id, @RequestBody LoanDto loanDto) {
        if (!id.equals(loanDto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(loanDtoConverter.convertToDto(loanService.updateLoan(loanDtoConverter.convertToEntity(loanDto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }
}