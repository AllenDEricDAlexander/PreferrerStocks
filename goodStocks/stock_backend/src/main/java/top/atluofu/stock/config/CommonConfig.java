package top.atluofu.stock.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import top.atluofu.stock.utils.IdWorker;
import top.atluofu.stock.vo.StockInfoConfig;

/**
 * @ClassName: CommonConfig
 * @description: 定义公共配置类
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-26Day-12:58
 * @Version: 1.0
 */
@Configuration
@EnableConfigurationProperties(StockInfoConfig.class)
public class CommonConfig {
    /**
     * 密码加密器
     * BCryptPasswordEncoder方法采用SHA-256对密码进行加密
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置id生成器bean
     *
     * @return
     */
    @Bean
    public IdWorker idWorker() {
        //基于运维人员对机房和机器的编号规划自行约定
        return new IdWorker(1l, 2l);
    }
}
