package top.atluofu.stock.listener;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.atluofu.stock.service.StockService;

import java.util.Date;

/**
 * @ClassName: MqListener
 * @description: 监听股票变化消息
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-28Day-11:01
 * @Version: 1.0
 */
// @Component
@Slf4j
public class MqListener {
    @Autowired
    private Cache<String, Object> caffeineCache;

    @Autowired
    private StockService stockService;

    /**
     * @param date
     * @throws Exception
     */
    @RabbitListener(queues = "innerMarketQueue")
    public void acceptInnerMarketInfo(Date date) throws Exception {
        //获取时间毫秒差值
        long diffTime = DateTime.now().getMillis() - new DateTime(date).getMillis();
        //超过一分钟告警
        if (diffTime > 60000) {
            log.error("采集国内大盘时间点：{},同步超时：{}ms", new DateTime(date).toString("yyyy-MM-dd HH:mm:ss"), diffTime);
        }
        //将缓存置为失效删除
        caffeineCache.invalidate("innerMarketInfosKey");
        //调用服务更新缓存
        stockService.getNewestInnerMarketInfos();
    }

}