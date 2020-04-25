package pl.plagodzinski.eurortvagdtask.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.plagodzinski.eurortvagdtask.user.dto.RegisterUserDTO;
import pl.plagodzinski.eurortvagdtask.user.dto.UserInfoDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void register(@RequestBody @Valid final RegisterUserDTO userDTO) {
        this.userService.register(userDTO);
    }

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDTO getInfo(@NotBlank @RequestParam(name = "pesel") final String pesel) {

        return this.userService.getInfo(pesel);
    }



}
