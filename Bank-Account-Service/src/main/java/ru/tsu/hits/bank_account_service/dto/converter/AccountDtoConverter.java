package ru.tsu.hits.bank_account_service.dto.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.tsu.hits.bank_account_service.dto.AccountDto;
import ru.tsu.hits.bank_account_service.model.Account;

@Component
public class AccountDtoConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public AccountDto convertToDto(Account account) {
        return modelMapper.map(account, AccountDto.class);
    }

    public Account convertToEntity(AccountDto accountDto) {
        return modelMapper.map(accountDto, Account.class);
    }
}