package Demo_Login.Demo.Services;

import Demo_Login.Demo.Configs.RedisKeys;
import Demo_Login.Demo.Exceptions.ExceptionsClasses.IncorrectUniqueCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class UniqueCodeService {

    private RedisKeys redisKeys;

    @Autowired
    private RedisTemplate<String, String> redisTemplateCodes;

    private final String codeKey = redisKeys.getUniqueCodeKeyPrefix();

    public String generateUniqueCode() {
        int code = ThreadLocalRandom.current().nextInt(0, 999999);
        String uniqueCode = String.format("%06d", code);
        redisTemplateCodes.opsForSet().add(codeKey, uniqueCode);
        return uniqueCode;
    }

    public void checkingUniqueCode(String code){
        if(!redisTemplateCodes.opsForSet().isMember(codeKey, code))
            throw new IncorrectUniqueCodeException("Уникальный код не является действительным");
    }

    public void deleteUniqueCode(String code){
        redisTemplateCodes.opsForSet().remove(codeKey, code);
    }
}
