package pl.plagodzinski.eurortvagdtask.currencyexchange;

public class AccountNotExistsException extends RuntimeException {
    private static final long serialVersionUID = 1431553427589260361L;
    private final String currencyCode;

    public AccountNotExistsException(final String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}
