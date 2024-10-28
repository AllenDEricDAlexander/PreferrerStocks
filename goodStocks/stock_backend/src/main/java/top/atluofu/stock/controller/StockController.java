package top.atluofu.stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.atluofu.stock.pojo.domain.InnerMarketDomain;
import top.atluofu.stock.pojo.domain.Stock4MinuteDomain;
import top.atluofu.stock.pojo.domain.StockBlockDomain;
import top.atluofu.stock.service.StockService;
import top.atluofu.stock.vo.R;
import top.atluofu.stock.vo.resp.PageResult;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: StockController
 * @description: A股大盘数据接口
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-28Day-9:10
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api/quot")
public class StockController {

    @Autowired
    private StockService stockService;

    /**
     * 获取国内最新大盘指数
     *
     * @return
     */
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> innerIndexAll() {
        return stockService.innerIndexAll();
    }

    /**
     * 需求说明: 获取沪深两市板块最新数据，以交易总金额降序查询，取前10条数据
     *
     * @return
     */
    @GetMapping("/sector/all")
    public R<List<StockBlockDomain>> sectorAll() {
        return stockService.sectorAllLimit();
    }

    /**
     * 分页查询股票最新数据，并按照涨幅排序查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/stock/all")
    public R<PageResult> getStockPageInfo(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                          @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        return stockService.getStockPageInfo(page, pageSize);
    }

    /**
     * 统计最新交易日下股票每分钟涨跌停的数量
     *
     * @return
     */
    @GetMapping("/stock/updown/count")
    public R<Map> getStockUpdownCount() {
        return stockService.getStockUpdownCount();
    }

    /**
     * 将指定页的股票数据导出到excel表下
     *
     * @param response
     * @param page     当前页
     * @param pageSize 每页大小
     */
    @GetMapping("/stock/export")
    public void stockExport(HttpServletResponse response, Integer page, Integer pageSize) {
        stockService.stockExport(response, page, pageSize);
    }

    /**
     * 功能描述：统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     *
     * @return
     */
    @GetMapping("/stock/tradeAmt")
    public R<Map> stockTradeVol4InnerMarket() {
        return stockService.stockTradeVol4InnerMarket();
    }

    /**
     * 查询当前时间下股票的涨跌幅度区间统计功能
     * 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询点
     *
     * @return
     */
    @GetMapping("/stock/updown")
    public R<Map> getStockUpDown() {
        return stockService.stockUpDownScopeCount();
    }

    /**
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
     * 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     *
     * @param code 股票编码
     * @return
     */
    @GetMapping("/stock/screen/time-sharing")
    public R<List<Stock4MinuteDomain>> stockScreenTimeSharing(String code) {
        return stockService.stockScreenTimeSharing(code);
    }


    /**
     * 单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据
     *
     * @param stockCode 股票编码
     */
    @RequestMapping("/stock/screen/dkline")
    public R<List<Map>> getDayKLinData(@RequestParam("code") String stockCode) {
        return stockService.stockCreenDkLine(stockCode);
    }
}