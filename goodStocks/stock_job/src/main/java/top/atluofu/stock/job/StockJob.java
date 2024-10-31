package top.atluofu.stock.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.atluofu.stock.serrvice.StockTimerTaskService;

/**
 * @ClassName: StockJob
 * @description: 定义股票相关数据的定时任务
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-31Day-12:27
 * @Version: 1.0
 */
@Component
public class StockJob {

    /**
     * 注入股票定时任务服务bean
     */
    @Autowired
    private StockTimerTaskService stockTimerTaskService;


    /**
     * 定义定时任务，采集国内大盘数据
     */
    @XxlJob("getStockInnerMarketInfos")
    public void getStockInnerMarketInfos() {
        stockTimerTaskService.getInnerMarketInfo();
    }

}