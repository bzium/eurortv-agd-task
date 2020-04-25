package pl.plagodzinski.eurortvagdtask.user;

import org.mapstruct.Mapper;
import pl.plagodzinski.eurortvagdtask.user.db.User;
import pl.plagodzinski.eurortvagdtask.user.dto.RegisterUserDTO;
import pl.plagodzinski.eurortvagdtask.user.dto.UserInfoDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User mapToEntity(RegisterUserDTO userDTO);
    UserInfoDTO mapToDto(User user);
}
