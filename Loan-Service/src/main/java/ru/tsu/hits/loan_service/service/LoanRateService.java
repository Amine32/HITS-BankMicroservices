package ru.tsu.hits.loan_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.tsu.hits.loan_service.dto.LoanRateDto;
import ru.tsu.hits.loan_service.model.LoanRate;
import ru.tsu.hits.loan_service.repository.LoanRateRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanRateService {

    private final LoanRateRepository loanRateRepository;
    private final IdempotencyCacheService idempotencyCacheService;

    private LoanRate createLoanRate(LoanRateDto loanRateDto) {
        LoanRate loanRate = LoanRate.builder()
                .name(loanRateDto.getName())
                .interestRate(loanRateDto.getInterestRate())
                .termLength(loanRateDto.getTermLength())
                .build();

        return loanRateRepository.save(loanRate);
    }

    public List<LoanRate> getAllLoanRates() {
        return loanRateRepository.findAll();
    }

    public Optional<LoanRate> getLoanRateById(Long id) {
        return loanRateRepository.findById(id);
    }

    @Transactional
    public LoanRate updateLoanRate(Long id, LoanRateDto loanRateDto) {
        LoanRate loanRate = loanRateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("LoanRate with id " + id + " not found"));

        loanRate.setName(loanRateDto.getName());
        loanRate.setInterestRate(loanRateDto.getInterestRate());
        loanRate.setTermLength(loanRateDto.getTermLength());

        return loanRateRepository.save(loanRate);
    }

    @Transactional
    public void deleteLoanRate(Long id) {
        loanRateRepository.deleteById(id);
    }

    public ResponseEntity<LoanRate> createLoanRateWithIdempotency(LoanRateDto loanRateDto, String idempotencyKey) {
        Object existingResponse = idempotencyCacheService.getResponse(idempotencyKey);
        if (existingResponse != null) {
            return ResponseEntity.ok((LoanRate) existingResponse);
        }

        LoanRate loanRate = createLoanRate(loanRateDto); // Actual creation logic
        idempotencyCacheService.storeResponse(idempotencyKey, loanRate);
        return ResponseEntity.ok(loanRate);
    }
}
