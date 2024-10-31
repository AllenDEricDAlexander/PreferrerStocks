package top.atluofu.stock.controller;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.atluofu.stock.serrvice.StockTimerTaskService;

/**
 * @ClassName: StockJobController
 * @description: StockJobController
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-31Day-12:29
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api/job")
public class StockJobController {

    final StockTimerTaskService stockTimerTaskService;

    public StockJobController(StockTimerTaskService stockTimerTaskService) {
        this.stockTimerTaskService = stockTimerTaskService;
    }

    /**
     * 定时采集A股数据
     */
    @XxlJob("getStockInfos")
    public void getStockInfos() {
        stockTimerTaskService.getStockRtIndex();
    }
}
