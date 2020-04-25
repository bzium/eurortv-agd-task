package pl.plagodzinski.eurortvagdtask.currencyexchange;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/money")
public class MoneyController {
    private final MoneyExchangeService moneyExchangeService;

    public MoneyController(final MoneyExchangeService moneyExchangeService) {
        this.moneyExchangeService = moneyExchangeService;
    }

    @PostMapping(value = "/exchange", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void register(@RequestBody @Valid final ExchangeRequestDTO exchangeRequestDTO) {
        moneyExchangeService.exchange(exchangeRequestDTO);
    }
}
