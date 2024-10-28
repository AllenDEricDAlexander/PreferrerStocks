package top.atluofu.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @ClassName: TestRedis
 * @description: 测试redis基础环境
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-28Day-8:39
 * @Version: 1.0
 */
@SpringBootTest
class TestRedis {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void test01() {
        String value = "myname";
        String key = "zhangsan";
        //存入值
        redisTemplate.opsForValue().set(key, value);
        //获取值
        String res = redisTemplate.opsForValue().get(key);
        assertEquals(value, res);
    }
}