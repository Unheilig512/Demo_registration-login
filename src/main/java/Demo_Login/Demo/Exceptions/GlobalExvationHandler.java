package Demo_Login.Demo.Exceptions;

import Demo_Login.Demo.DTO.ErrorMessage;
import Demo_Login.Demo.Exceptions.ExceptionsClasses.IncorrectUniqueCodeException;
import Demo_Login.Demo.Exceptions.ExceptionsClasses.UserAlreadyExistException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExvationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationException(MethodArgumentNotValidException ex){
        String error = ex.getBindingResult()
                //1 вариант - возвращаем все ошибки
//                .getFieldErrors()
//                .stream()
//                .map(err -> err.getField() + ": " + err.getDefaultMessage())
//                .collect(Collectors.toList());
                //2 вариант - возвращаем только первое сообщение об ошибке
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();
        return ResponseEntity.badRequest().body(new ErrorMessage("Ошибка ввода данных", error));
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorMessage> handleUserAlreadyExist(UserAlreadyExistException ex){
        return ResponseEntity.badRequest().body(new ErrorMessage("Пользователь уже существует", ex.getMessage()));
    }

    @ExceptionHandler(IncorrectUniqueCodeException.class)
    public ResponseEntity<ErrorMessage> handleIncorrectUniqueCode(IncorrectUniqueCodeException ex){
        return ResponseEntity.badRequest().body(new ErrorMessage("Неверный уникальный код", ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessage> handleBadCredentials(BadCredentialsException ex){
        return ResponseEntity.status(401).body(new ErrorMessage("Неверные учетные данные", ex.getMessage()));
    }

}
