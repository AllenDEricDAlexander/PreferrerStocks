package top.atluofu.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @ClassName: TestRedis
 * @description: 测试redis基础环境
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-28Day-8:39
 * @Version: 1.0
 */
@SpringBootTest
public class TestRedis {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void test01() {
        //存入值
        redisTemplate.opsForValue().set("myname", "zhangsan");
        //获取值
        String myname = redisTemplate.opsForValue().get("myname");
        System.out.println(myname);
    }
}