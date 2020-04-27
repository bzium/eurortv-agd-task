package pl.plagodzinski.eurortvagdtask.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.plagodzinski.eurortvagdtask.user.db.MoneyAccount;
import pl.plagodzinski.eurortvagdtask.user.db.MoneyAccountRepository;
import pl.plagodzinski.eurortvagdtask.user.db.User;
import pl.plagodzinski.eurortvagdtask.user.db.UserRepository;
import pl.plagodzinski.eurortvagdtask.user.dto.RegisterUserDTO;
import pl.plagodzinski.eurortvagdtask.user.dto.UserInfoDTO;

import javax.transaction.Transactional;

@Service
public class UserService {

    private static final String DEFAULT_CURRENCY_NAME = "PLN";
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final MoneyAccountRepository moneyAccountRepository;
    private final UserMapper userMapper;

    public UserService(final UserRepository userRepository, final MoneyAccountRepository moneyAccountRepository, final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.moneyAccountRepository = moneyAccountRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public void register(final RegisterUserDTO userDTO) {
        isUserExists(userDTO);

        final User user = userMapper.mapToEntity(userDTO);

        final MoneyAccount userMoneyAccount = new MoneyAccount();
        userMoneyAccount.setAmount(userDTO.getStartAmount());
        userMoneyAccount.setCurrencyCode(DEFAULT_CURRENCY_NAME);

        userMoneyAccount.setUser(user);

        userRepository.save(user);
        moneyAccountRepository.save(userMoneyAccount);

        LOG.info("User {} registered successfully", userDTO.getPesel());
    }

    private void isUserExists(final RegisterUserDTO userDTO) {
        if (userRepository.countByPesel(userDTO.getPesel()) > 0) {
            throw new AlreadyExistsException();
        }
    }

    public UserInfoDTO getInfo(final String pesel) {
        final User user = userRepository.getByPesel(pesel);
        return userMapper.mapToDto(user);
    }
}
