package pl.plagodzinski.eurortvagdtask.currencyexchange;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.pl.PESEL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ExchangeRequestDTO {

    @PESEL
    @NotNull
    private final String pesel;

    @NotNull
    private final BigDecimal amount;

    @NotNull
    @NotBlank
    private final String fromCurrencyCode;

    @NotNull
    @NotBlank
    @Length(min = 3, max = 3)
    private final String toCurrencyCode;

    public ExchangeRequestDTO(final String pesel, final BigDecimal amount, final String fromCurrencyCode, final String toCurrencyCode) {
        this.pesel = pesel;
        this.amount = amount;
        this.fromCurrencyCode = fromCurrencyCode;
        this.toCurrencyCode = toCurrencyCode;
    }

    public String getPesel() {
        return pesel;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getFromCurrencyCode() {
        return fromCurrencyCode;
    }

    public String getToCurrencyCode() {
        return toCurrencyCode;
    }
}
