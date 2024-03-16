package ru.tsu.hits.loan_service.dto.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.tsu.hits.loan_service.dto.LoanDto;
import ru.tsu.hits.loan_service.model.Loan;

@Component
public class LoanDtoConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public LoanDto convertToDto(Loan loan) {
        return modelMapper.map(loan, LoanDto.class);
    }

    public Loan convertToEntity(LoanDto loanDto) {
        return modelMapper.map(loanDto, Loan.class);
    }
}