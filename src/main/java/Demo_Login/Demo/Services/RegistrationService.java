package Demo_Login.Demo.Services;

import Demo_Login.Demo.Configs.RedisKeys;
import Demo_Login.Demo.DTO.RegisterRequest;
import Demo_Login.Demo.Exceptions.ExceptionsClasses.IncorrectUniqueCodeException;
import Demo_Login.Demo.Exceptions.ExceptionsClasses.UserAlreadyExistException;
import Demo_Login.Demo.Models.User;
import Demo_Login.Demo.Repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    @Autowired
    private RedisKeys redisKeys;

    @Autowired
    private RedisTemplate<String, String> redisTemplateCodes;

    private final String codeKey = redisKeys.getUniqueCodeKeyPrefix();
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

