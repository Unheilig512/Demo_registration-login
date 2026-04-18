package Demo_Login.Demo.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RedisKeys {
    private static final String LOGIN_ATTEMPTS_KEY_PREFIX = "login_attempts:";
    private static final String UNIQUE_CODE_KEY_PREFIX = "unique_code:";


    public static String getLoginAttemptsKeyPrefix(){
        return LOGIN_ATTEMPTS_KEY_PREFIX;
    }

    public static String getUniqueCodeKeyPrefix(){
        return UNIQUE_CODE_KEY_PREFIX;
    }
}
