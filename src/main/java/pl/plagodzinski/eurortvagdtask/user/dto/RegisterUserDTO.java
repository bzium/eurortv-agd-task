package pl.plagodzinski.eurortvagdtask.user.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.pl.PESEL;
import pl.plagodzinski.eurortvagdtask.user.dto.validator.Adult;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public class RegisterUserDTO implements Serializable {

    private static final long serialVersionUID = -970421552581092523L;

    @NotBlank
    @NotNull
    @Length(max = 30)
    private final String name;

    @NotBlank
    @NotNull
    @Length(max = 30)
    private final String surname;

    @NotNull
    @PESEL
    @Adult
    private final String pesel;

    @NotNull
    @Min(0)
    @Digits(integer = 3, fraction = 2)
    private final BigDecimal startAmount;

    public RegisterUserDTO(final String name, final String surname, final String pesel, final  BigDecimal startAmount) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.startAmount = startAmount;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getPesel() {
        return this.pesel;
    }

    public BigDecimal getStartAmount() {
        return this.startAmount;
    }
}
