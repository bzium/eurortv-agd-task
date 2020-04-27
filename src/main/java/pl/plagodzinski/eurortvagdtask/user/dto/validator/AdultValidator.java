package pl.plagodzinski.eurortvagdtask.user.dto.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.DateTimeException;
import java.time.LocalDate;

public class AdultValidator implements ConstraintValidator<Adult, String> {

    private static final int ADULT_AGE = 18;
    private static final Logger LOG = LoggerFactory.getLogger(AdultValidator.class);

    @Override
    public void initialize(final Adult constraintAnnotation) {
        LOG.info("Adult validator initialised");
    }

    @Override
    public boolean isValid(final String pesel, final ConstraintValidatorContext constraintValidatorContext) {
        if(pesel.length() == 11) {
            return isAdult(pesel);
        }
        return false;
    }

    private boolean isAdult(final String pesel) {
        try {
            final LocalDate dateOfBirth = calculateBirthDate(pesel);
            return LocalDate
                    .now()
                    .minusYears(ADULT_AGE)
                    .isAfter(dateOfBirth);
        } catch (final DateTimeException e) {
            LOG.error("Error when calculate birth date",e);
            return false;
        }
    }

    private LocalDate calculateBirthDate(final String pesel) {
        final int[] numberAsIntArray = getPeselAsArray(pesel);

        final int birthDay = getBirthDay(numberAsIntArray);
        int birthYear = getBirthYear(numberAsIntArray);
        int birthMonth = getBirthMonth(numberAsIntArray);

        if (birthMonth <= 12) {
            birthYear += 1900;
        } else if (birthMonth <= 32) {
            birthYear += 2000;
            birthMonth -= 20;
        } else if (birthMonth <= 52) {
            birthYear += 2100;
            birthMonth -= 40;
        } else if (birthMonth <= 72) {
            birthYear += 2200;
            birthMonth -= 60;
        } else if (birthMonth <= 92) {
            birthYear += 1800;
            birthMonth -= 80;
        }

        return LocalDate.of(birthYear, birthMonth, birthDay);
    }

    private int getBirthMonth(final int[] numberAsIntArray) {
        return 10 * numberAsIntArray[2] + numberAsIntArray[3];
    }

    private int getBirthYear(final int[] numberAsIntArray) {
        return 10 * numberAsIntArray[0] + numberAsIntArray[1];
    }

    private int getBirthDay(final int[] numberAsIntArray) {
        return 10 * numberAsIntArray[4] + numberAsIntArray[5];
    }

    private int[] getPeselAsArray(final String pesel) {
        final int[] numberAsIntArray = new int[11];
        for (int i = 0; i < 11; i++) {
            numberAsIntArray[i] = Character.getNumericValue(pesel.charAt(i));
        }
        return numberAsIntArray;
    }

}
