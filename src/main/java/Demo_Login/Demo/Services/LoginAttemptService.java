package Demo_Login.Demo.Services;

import Demo_Login.Demo.Configs.RedisKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class LoginAttemptService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final String loginKey = RedisKeys.getLoginAttemptsKeyPrefix();

    private final int MAX_ATTEMPT = 5;
    private final Duration BLOCK_DURATION = Duration.ofMinutes(15);

    public String key(String type, String value){
        return RedisKeys.getLoginAttemptsKeyPrefix() + type + ":" + value;
    }

    public void loginFailed(String username,String fingerprint){
        updateCounter(loginKey + "user:" + username);
        updateCounter(loginKey + "fp:" + fingerprint);
    }

    public void loginSucceeded(String username, String fingerprint){
        redisTemplate.delete(key("user", username));
        redisTemplate.delete(key("fp", fingerprint));
    }

    public boolean isBlocked(String username, String fingerprint){
        return checkKey(fingerprint, "fp") || checkKey(username, "user");
    }

    private void updateCounter(String key) {
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, BLOCK_DURATION);
    }

    private boolean checkKey(String value, String type){
        String attemps = redisTemplate.opsForValue().get(loginKey + type + value);
        return attemps != null && Integer.parseInt(attemps) >= MAX_ATTEMPT;
    }

}
