package ru.tsu.hits.core_service.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.tsu.hits.core_service.model.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CurrencyConversionService {

    private final RestTemplate restTemplate;

    public CurrencyConversionService() {
        this.restTemplate = new RestTemplate();
    }

    public BigDecimal convert(Currency from, Currency to, BigDecimal amount) {
        if (from == to) {
            return amount;
        }

        String apiKey = "4da029f2691803929217bdb8";
        String apiUrl = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/";
        String url = apiUrl + from + "/" + to + "/" + amount;

        ResponseEntity<ExchangeRateResponse> response = restTemplate.getForEntity(url, ExchangeRateResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            ExchangeRateResponse responseBody = response.getBody();
            if ("success".equals(responseBody.getResult())) {
                return BigDecimal.valueOf(responseBody.getConversionResult()).setScale(2, RoundingMode.HALF_UP);
            }
        }

        throw new IllegalStateException("Could not fetch exchange rates or API returned an error");
    }


    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ExchangeRateResponse {
        @JsonProperty("result")
        private String result;

        @JsonProperty("conversion_rate")
        private BigDecimal conversionRate;

        @JsonProperty("conversion_result")
        private Double conversionResult;
    }

}

