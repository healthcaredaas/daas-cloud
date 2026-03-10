package cn.healthcaredaas.data.cloud.auth.cache;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;

import java.time.Duration;

/**

 * @ClassName： SignInFailureLimitedCacher.java
 * @Author： chenpan
 * @Date：2024/12/15 16:33
 * @Modify：
 */
public class SignInFailureLimitedContainer {

    public static final String CACHE_SIGN_IN_FAILURE = "sign_in:failure_limited:";

    private RedissonClient redissonClient;

    public SignInFailureLimitedContainer(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private String key(String username) {
        return CACHE_SIGN_IN_FAILURE + username;
    }

    public int count(String username) {
        RAtomicLong rAtomicLong = redissonClient.getAtomicLong(key(username));
        long count = rAtomicLong.get();
        return (int) count;
    }

    public int increment(String username, Duration expire) {
        RAtomicLong rAtomicLong = redissonClient.getAtomicLong(key(username));
        rAtomicLong.expire(expire);
        long count = rAtomicLong.incrementAndGet();
        return (int) count;
    }

    public void delete(String username) {
        RAtomicLong rAtomicLong = redissonClient.getAtomicLong(key(username));
        rAtomicLong.delete();
    }
}