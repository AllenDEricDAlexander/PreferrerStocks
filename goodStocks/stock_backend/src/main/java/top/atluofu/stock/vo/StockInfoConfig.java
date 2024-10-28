package top.atluofu.stock.vo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @ClassName: StockInfoConfig
 * @description: 股票常用的公共参数 动态封装
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-28Day-9:07
 * @Version: 1.0
 */
@ConfigurationProperties(prefix = "stock")
@Data
public class StockInfoConfig {
    //A股大盘ID集合
    private List<String> inner;
    //外盘ID集合
    private List<String> outer;
    //股票区间
    private List<String> upDownRange;
}