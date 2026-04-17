package Demo_Login.Demo.Services;

import Demo_Login.Demo.Configs.RedisKeys;
import Demo_Login.Demo.Exceptions.ExceptionsClasses.IncorrectUniqueCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class UniqueCodeService {

    private final RedisKeys redisKeys;
    private final StringRedisTemplate redisTemplateCodes;

    public String generateUniqueCode() {
        int code = ThreadLocalRandom.current().nextInt(0, 999999);
        String uniqueCode = String.format("%06d", code);
        redisTemplateCodes.opsForSet().add(redisKeys.getUniqueCodeKeyPrefix(), uniqueCode);
        return uniqueCode;
    }

    public void checkingUniqueCode(String code){
        if(!redisTemplateCodes.opsForSet().isMember(redisKeys.getUniqueCodeKeyPrefix(), code))
            throw new IncorrectUniqueCodeException("Уникальный код не является действительным");
    }

    public void deleteUniqueCode(String code){
        redisTemplateCodes.opsForSet().remove(redisKeys.getUniqueCodeKeyPrefix(), code);
    }
}
