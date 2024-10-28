package top.atluofu.stock.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.atluofu.stock.utils.IdWorker;
import top.atluofu.stock.utils.ParserStockInfoUtil;
import top.atluofu.stock.vo.StockInfoConfig;

/**
 * @ClassName: CommonConfig
 * @description: 配置ID生成器bean
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-28Day-10:43
 * @Version: 1.0
 */
@Configuration
@EnableConfigurationProperties(StockInfoConfig.class)
public class CommonConfig {
    /**
     * 配置基于雪花算法生成全局唯一id
     * 参与元算的参数： 时间戳 + 机房id + 机器id + 序列号
     * 保证id唯一
     *
     * @return
     */
    @Bean
    public IdWorker idWorker() {
        //指定当前为1号机房的2号机器生成
        return new IdWorker(2L, 1L);
    }

    /**
     * 配置解析工具bean
     *
     * @param idWorker
     * @return
     */
    @Bean
    public ParserStockInfoUtil parserStockInfoUtil(IdWorker idWorker) {
        return new ParserStockInfoUtil(idWorker);
    }
}