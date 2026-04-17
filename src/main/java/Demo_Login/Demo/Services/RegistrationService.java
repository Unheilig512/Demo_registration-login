package Demo_Login.Demo.Services;

import Demo_Login.Demo.DTO.RegisterRequest;
import Demo_Login.Demo.Exceptions.ExceptionsClasses.UserAlreadyExistException;
import Demo_Login.Demo.Models.User;
import Demo_Login.Demo.Repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(@Valid RegisterRequest userDto) throws UserAlreadyExistException {
        if(userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("Username already exists in the database");
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }

}
