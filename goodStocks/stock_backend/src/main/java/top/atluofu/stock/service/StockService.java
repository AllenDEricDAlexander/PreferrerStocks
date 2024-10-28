package top.atluofu.stock.service;

import top.atluofu.stock.pojo.domain.InnerMarketDomain;
import top.atluofu.stock.pojo.domain.Stock4MinuteDomain;
import top.atluofu.stock.pojo.domain.StockBlockDomain;
import top.atluofu.stock.vo.R;
import top.atluofu.stock.vo.resp.PageResult;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: StockService
 * @description: 定义股票服务接口
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-28Day-9:11
 * @Version: 1.0
 */
public interface StockService {

    /**
     * 获取国内大盘的实时数据
     *
     * @return
     */
    R<List<InnerMarketDomain>> innerIndexAll();

    R<List<StockBlockDomain>> sectorAllLimit();

    R<PageResult> getStockPageInfo(Integer page, Integer pageSize);

    R<Map> getStockUpdownCount();

    void stockExport(HttpServletResponse response, Integer page, Integer pageSize);

    R<Map> stockTradeVol4InnerMarket();

    R<Map> stockUpDownScopeCount();

    R<List<Stock4MinuteDomain>> stockScreenTimeSharing(String code);

    R<List<Map>> stockCreenDkLine(String stockCode);
}