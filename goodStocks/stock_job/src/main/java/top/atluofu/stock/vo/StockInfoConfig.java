package top.atluofu.stock.vo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @ClassName: StockInfoConfig
 * @description: TODO
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-28Day-10:45
 * @Version: 1.0
 */
@ConfigurationProperties(prefix = "stock")
@Data
public class StockInfoConfig {
    //a股大盘ID集合
    private List<String> inner;
    //外盘ID集合
    private List<String> outer;
    //大盘参数获取url
    private String marketUrl;
    //板块参数获取url
    private String blockUrl;
}