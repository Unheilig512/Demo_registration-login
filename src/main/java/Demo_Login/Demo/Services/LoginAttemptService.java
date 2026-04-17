package Demo_Login.Demo.Services;

import Demo_Login.Demo.Configs.RedisKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {

    private static final int MAX_ATTEMPT = 5;
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(15);

    private final StringRedisTemplate redisTemplate;
    private final RedisKeys redisKeys;

    private String key(String type, String value) {
        return redisKeys.getLoginAttemptsKeyPrefix() + type + ":" + value;
    }


    public void loginFailed(String username,String fingerprint){
        updateCounter(key("user", username));
        updateCounter(key("fp", fingerprint));
    }

    public void loginSucceeded(String username, String fingerprint){
        redisTemplate.delete(key("user", username));
        redisTemplate.delete(key("fp", fingerprint));
    }

    public boolean isBlocked(String username, String fingerprint){
        return checkKey("fp", fingerprint) || checkKey("user", username);
    }

    private void updateCounter(String key) {
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, BLOCK_DURATION);
    }

    private boolean checkKey(String type, String value){
        String attempts = redisTemplate.opsForValue().get(key(type, value));
        return attempts != null && Integer.parseInt(attempts) >= MAX_ATTEMPT;
    }

}
