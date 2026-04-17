package Demo_Login.Demo.Controllers;

import Demo_Login.Demo.DTO.LoginRequest;
import Demo_Login.Demo.DTO.RegisterRequest;
import Demo_Login.Demo.Exceptions.ExceptionsClasses.IncorrectUniqueCodeException;
import Demo_Login.Demo.Services.LoginAttemptService;
import Demo_Login.Demo.Services.RegistrationService;
import Demo_Login.Demo.Services.UniqueCodeService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UniqueCodeService uniqueCodeService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String fingerPrint = loginRequest.getFingerprint();
        String username = loginRequest.getUsername();

        if (loginAttemptService.isBlocked(username, fingerPrint)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Слишком много неуданых попыток входа. Попробуйте позже.");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        loginAttemptService.loginSucceeded(username, fingerPrint);
        return ResponseEntity.ok("Успешный вход");

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        uniqueCodeService.checkingUniqueCode(registerRequest.getCode()); {
        registrationService.register(registerRequest);
        uniqueCodeService.deleteUniqueCode(registerRequest.getCode());
        return ResponseEntity.ok("Пользователь успешно зарегистрирован");
        }

    }
}
