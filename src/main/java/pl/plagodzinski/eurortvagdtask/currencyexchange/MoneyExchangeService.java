package pl.plagodzinski.eurortvagdtask.currencyexchange;

import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.plagodzinski.eurortvagdtask.user.db.MoneyAccount;
import pl.plagodzinski.eurortvagdtask.user.db.MoneyAccountRepository;
import pl.plagodzinski.eurortvagdtask.user.db.UserRepository;

import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class MoneyExchangeService {

    private static final BigDecimal START_AMOUNT_FOR_NEW_ACCOUNT = new BigDecimal("0.0");
    private static final Logger LOG = LoggerFactory.getLogger(MoneyExchangeService.class);
    private static final List<String> supportedCurrencyCodes = Arrays.asList("PLN", "USD");

    private final MoneyAccountRepository moneyAccountRepository;
    private final UserRepository userRepository;

    public MoneyExchangeService(final MoneyAccountRepository moneyAccountRepository, final UserRepository userRepository) {
        this.moneyAccountRepository = moneyAccountRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void exchange(final ExchangeRequestDTO exchangeRequestDTO) {

        checkIfCurrencyIsSupported(exchangeRequestDTO);

        final MoneyAccount fromMoneyAccount = checkIfBasicMoneyAccountIsValid(exchangeRequestDTO);
        final MoneyAccount toMoneyAccount = checkIfTargetAccountExistsAndCreateIfNot(exchangeRequestDTO);

        transformMoney(fromMoneyAccount, toMoneyAccount, exchangeRequestDTO.getAmount());

        moneyAccountRepository.save(fromMoneyAccount);
        moneyAccountRepository.save(toMoneyAccount);
    }

    private void checkIfCurrencyIsSupported(final ExchangeRequestDTO exchangeRequestDTO) {
        if(!supportedCurrencyCodes.contains(exchangeRequestDTO.getFromCurrencyCode()) ||
                !supportedCurrencyCodes.contains(exchangeRequestDTO.getToCurrencyCode())) {
            throw new UnsupportedOperationException();
        }
    }

    private MoneyAccount checkIfBasicMoneyAccountIsValid(final ExchangeRequestDTO exchangeRequestDTO) {
        final MoneyAccount fromMoneyAmount = findUserMoneyAccount(exchangeRequestDTO.getPesel(), exchangeRequestDTO.getFromCurrencyCode());
        if (fromMoneyAmount == null) {
            throw new AccountNotExistsException(exchangeRequestDTO.getFromCurrencyCode());
        }

        if (exchangeRequestDTO.getAmount().compareTo(fromMoneyAmount.getAmount()) > 0) {
            throw new InsufficientFundsException();
        }

        return fromMoneyAmount;
    }

    private MoneyAccount checkIfTargetAccountExistsAndCreateIfNot(final ExchangeRequestDTO exchangeRequestDTO) {
        MoneyAccount toMoneyAmount = findUserMoneyAccount(exchangeRequestDTO.getPesel(), exchangeRequestDTO.getToCurrencyCode());
        if(toMoneyAmount == null) {
            LOG.info("Not found account with currency {} for user {} Create new", exchangeRequestDTO.getToCurrencyCode(), exchangeRequestDTO.getPesel());
            toMoneyAmount = new MoneyAccount();
            toMoneyAmount.setUser(userRepository.getByPesel(exchangeRequestDTO.getPesel()));
            toMoneyAmount.setCurrencyCode(exchangeRequestDTO.getToCurrencyCode());
            toMoneyAmount.setAmount(START_AMOUNT_FOR_NEW_ACCOUNT);
        }
        return toMoneyAmount;
    }

    private MoneyAccount findUserMoneyAccount(final String pesel, final String currencyCode) {
        return moneyAccountRepository.findByUserAndCurrencyName(pesel, currencyCode.toUpperCase());
    }

    private void transformMoney(final MoneyAccount fromAccount, final MoneyAccount toAccount, final BigDecimal amount) {
        Money fromMoneyAccount = Money.of(fromAccount.getAmount(), fromAccount.getCurrencyCode());
        Money toMoneyAccount = Money.of(toAccount.getAmount(), toAccount.getCurrencyCode());
        final Money moneyAmountToExchange = Money.of(amount, fromAccount.getCurrencyCode());
        final MonetaryAmount moneyToAddTargetAccount = convertToTargetCurrency(moneyAmountToExchange, toAccount.getCurrencyCode());

        fromMoneyAccount = fromMoneyAccount.subtract(moneyAmountToExchange);
        fromAccount.setAmount(convertToBigDecimal(fromMoneyAccount));

        toMoneyAccount = toMoneyAccount.add(moneyToAddTargetAccount);
        toAccount.setAmount(convertToBigDecimal(toMoneyAccount));
    }

    private BigDecimal convertToBigDecimal(final MonetaryAmount monetaryAmount) {
        return monetaryAmount.getNumber().numberValueExact(BigDecimal.class);
    }

    private MonetaryAmount convertToTargetCurrency(final MonetaryAmount monetaryAmountToExchange, final String targetCurrencyCode) {
        final CurrencyConversion conversion = MonetaryConversions.getConversion(targetCurrencyCode);
        return monetaryAmountToExchange.with(conversion);
    }
}
