package top.atluofu.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @ClassName: TestAll
 * @description: 测试类
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-26Day-12:59
 * @Version: 1.0
 */
@SpringBootTest
public class TestAll {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testPwd() {
        String pwd = "1234";
        //加密  $2a$10$WAWV.QEykot8sHQi6FqqDOAnevkluOZJqZJ5YPxSnVVWqvuhx88Ha
        String encode = passwordEncoder.encode(pwd);
        System.out.println(encode);
        /*
            matches()匹配明文密码和加密后密码是否匹配，如果匹配，返回true，否则false
            just test
         */
        boolean flag = passwordEncoder.matches(pwd, "$2a$10$WAWV.QEykot8sHQi6FqqDOAnevkluOZJqZJ5YPxSnVVWqvuhx88Ha");
        System.out.println(flag);
    }
}
