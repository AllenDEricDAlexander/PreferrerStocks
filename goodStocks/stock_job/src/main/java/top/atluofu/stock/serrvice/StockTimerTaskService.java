package top.atluofu.stock.serrvice;

/**
 * @ClassName: StockTimerTaskService
 * @description: 定义采集股票数据的定时任务的服务接口
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-28Day-10:45
 * @Version: 1.0
 */
public interface StockTimerTaskService {
    /**
     * 获取国内大盘的实时数据信息
     */
    void getInnerMarketInfo();

    /**
     * 定义获取分钟级股票数据
     */
    void getStockRtIndex();
}