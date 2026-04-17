package Demo_Login.Demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Lazy;

import java.util.Date;

@Lazy
@Data
@AllArgsConstructor
public class ErrorMessage {
    private String message;
    private String description;
}
