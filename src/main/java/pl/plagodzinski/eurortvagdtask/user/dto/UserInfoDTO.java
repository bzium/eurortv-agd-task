package pl.plagodzinski.eurortvagdtask.user.dto;

import java.util.ArrayList;
import java.util.List;

public class UserInfoDTO {
    public String name;
    public String surname;
    public List<MoneyAccountDTO> moneyAccountList = new ArrayList<>();
}
