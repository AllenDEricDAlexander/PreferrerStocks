package top.atluofu.stock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName: HttpClientConfig
 * @description: 定义访问http服务的配置类
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-28Day-10:43
 * @Version: 1.0
 */
@Configuration
public class HttpClientConfig {
    /**
     * 定义restTemplate bean
     *
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
