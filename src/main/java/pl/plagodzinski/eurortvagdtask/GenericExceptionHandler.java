package pl.plagodzinski.eurortvagdtask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.plagodzinski.eurortvagdtask.currencyexchange.AccountNotExistsException;
import pl.plagodzinski.eurortvagdtask.currencyexchange.InsufficientFundsException;
import pl.plagodzinski.eurortvagdtask.currencyexchange.NoSupportedCurrencyException;
import pl.plagodzinski.eurortvagdtask.user.AlreadyExistsException;

import javax.money.UnknownCurrencyException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

@ControllerAdvice
public class GenericExceptionHandler {

    private final Logger LOG = LoggerFactory.getLogger(GenericExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationException(final HttpServletResponse response, final ConstraintViolationException e) throws IOException {
        LOG.error("Error when parse request",e);
        response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<String> handleAlreadyExistsException() {
        return new ResponseEntity<>("User already exists exception", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFundsException() {
        return new ResponseEntity<>("User has insufficient funds on account", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotExistsException.class)
    public ResponseEntity<String> handleAccountNotExistsException(final AccountNotExistsException e) {
        return new ResponseEntity<>("Account for currency " + e.getCurrencyCode() + " doesn't exists" , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnknownCurrencyException.class)
    public ResponseEntity<String> handleUnknownCurrencyException(final UnknownCurrencyException e) {
        return new ResponseEntity<>("Unknown currency " + e.getCurrencyCode(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSupportedCurrencyException.class)
    public ResponseEntity<String> handleUnknownCurrencyException() {
        return new ResponseEntity<>("One of more currencies are unsupported", HttpStatus.BAD_REQUEST);
    }
}
