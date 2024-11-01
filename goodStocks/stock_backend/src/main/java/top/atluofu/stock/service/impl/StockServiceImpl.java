package top.atluofu.stock.service.impl;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.atluofu.stock.pojo.domain.InnerMarketDomain;
import top.atluofu.stock.pojo.domain.Stock4EvrDayDomain;
import top.atluofu.stock.pojo.domain.Stock4MinuteDomain;
import top.atluofu.stock.pojo.domain.StockBlockDomain;
import top.atluofu.stock.pojo.domain.StockUpdownDomain;
import top.atluofu.stock.pojo.mapper.StockBlockRtInfoMapper;
import top.atluofu.stock.pojo.mapper.StockBusinessMapper;
import top.atluofu.stock.pojo.mapper.StockMarketIndexInfoMapper;
import top.atluofu.stock.pojo.mapper.StockRtInfoMapper;
import top.atluofu.stock.service.StockService;
import top.atluofu.stock.utils.DateTimeUtil;
import top.atluofu.stock.vo.R;
import top.atluofu.stock.vo.ResponseCode;
import top.atluofu.stock.vo.StockInfoConfig;
import top.atluofu.stock.vo.resp.PageResult;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ClassName: StockServiceImpl
 * @description: TODO
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-28Day-9:11
 * @Version: 1.0
 */
@Slf4j
@Service("stockService")
public class StockServiceImpl implements StockService {

    private final StockBusinessMapper stockBusinessMapper;
    private final StockRtInfoMapper stockRtInfoMapper;
    private final StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
    private final StockBlockRtInfoMapper stockBlockRtInfoMapper;
    private final Cache<String, Object> caffeineCache;
    private final StockInfoConfig stockInfoConfig;

    public StockServiceImpl(StockBusinessMapper stockBusinessMapper, StockMarketIndexInfoMapper stockMarketIndexInfoMapper, StockBlockRtInfoMapper stockBlockRtInfoMapper, StockInfoConfig stockInfoConfig, StockRtInfoMapper stockRtInfoMapper, Cache<String, Object> caffeineCache) {
        this.stockBusinessMapper = stockBusinessMapper;
        this.stockMarketIndexInfoMapper = stockMarketIndexInfoMapper;
        this.stockBlockRtInfoMapper = stockBlockRtInfoMapper;
        this.stockInfoConfig = stockInfoConfig;
        this.stockRtInfoMapper = stockRtInfoMapper;
        this.caffeineCache = caffeineCache;
    }

    /**
     * 获取国内大盘的实时数据
     *
     * @return
     */
    @Override
    public R<List<InnerMarketDomain>> innerIndexAll() {
        //1.获取国内A股大盘的id集合
        List<String> inners = stockInfoConfig.getInner();
        //2.获取最近股票交易日期
        Date lastDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //TODO mock测试数据，后期数据通过第三方接口动态获取实时数据 可删除
        lastDate = DateTime.parse("2022-01-02 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //3.将获取的java Date传入接口
        List<InnerMarketDomain> list = stockMarketIndexInfoMapper.getMarketInfo(inners, lastDate);
        //4.返回查询结果
        return R.ok(list);
    }

    /**
     * 需求说明: 沪深两市板块分时行情数据查询，以交易时间和交易总金额降序查询，取前10条数据
     *
     * @return
     */
    @Override
    public R<List<StockBlockDomain>> sectorAllLimit() {

        //获取股票最新交易时间点
        Date lastDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //TODO mock数据,后续删除
        lastDate = DateTime.parse("2021-12-21 14:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //1.调用mapper接口获取数据
        List<StockBlockDomain> infos = stockBlockRtInfoMapper.sectorAllLimit(lastDate);
        //2.组装数据
        if (CollectionUtils.isEmpty(infos)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(infos);
    }

    /**
     * 分页查询股票最新数据，并按照涨幅排序查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public R<PageResult> getStockPageInfo(Integer page, Integer pageSize) {
        //1.设置PageHelper分页参数
        PageHelper.startPage(page, pageSize);
        //2.获取当前最新的股票交易时间点
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //todo
        curDate = DateTime.parse("2022-06-07 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //3.调用mapper接口查询
        List<StockUpdownDomain> infos = stockRtInfoMapper.getNewestStockInfo(curDate);
        if (CollectionUtils.isEmpty(infos)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA);
        }
        //4.组装PageInfo对象，获取分页的具体信息,因为PageInfo包含了丰富的分页信息，而部分分页信息是前端不需要的
        //PageInfo<StockUpdownDomain> pageInfo = new PageInfo<>(infos);
//        PageResult<StockUpdownDomain> pageResult = new PageResult<>(pageInfo);
        PageResult<StockUpdownDomain> pageResult = new PageResult<>(new PageInfo<>(infos));
        //5.封装响应数据
        return R.ok(pageResult);
    }

    /**
     * 统计最新交易日下股票每分钟涨跌停的数量
     *
     * @return
     */
    @Override
    public R<Map> getStockUpdownCount() {
        //1.获取最新的交易时间范围 openTime  curTime
        //1.1 获取最新股票交易时间点
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date curTime = curDateTime.toDate();

        curTime = DateTime.parse("2022-01-06 14:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //1.2 获取最新交易时间对应的开盘时间
        DateTime openDate = DateTimeUtil.getOpenDate(curDateTime);
        Date openTime = openDate.toDate();

        openTime = DateTime.parse("2022-01-06 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //2.查询涨停数据
        //约定mapper中flag入参： 1-》涨停数据 0：跌停
        List<Map> upCounts = stockRtInfoMapper.getStockUpdownCount(openTime, curTime, 1);
        //3.查询跌停数据
        List<Map> dwCounts = stockRtInfoMapper.getStockUpdownCount(openTime, curTime, 0);
        //4.组装数据
        HashMap<String, List> mapInfo = new HashMap<>();
        mapInfo.put("upList", upCounts);
        mapInfo.put("downList", dwCounts);
        //5.返回结果
        return R.ok(mapInfo);
    }

    /**
     * 将指定页的股票数据导出到excel表下
     *
     * @param response
     * @param page     当前页
     * @param pageSize 每页大小
     */
    @Override
    public void stockExport(HttpServletResponse response, Integer page, Integer pageSize) {
        //1.获取分页数据
        Date lastDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //TODO 伪造数据，后续删除
        lastDate = DateTime.parse("2022-07-07 14:43:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        PageHelper.startPage(page, pageSize);
        List<StockUpdownDomain> infos = stockRtInfoMapper.getStockUpDownInfos(lastDate);
        response.setCharacterEncoding("utf-8");
        try {
            //2.判断分页数据是否为空，为空则响应json格式的提示信息
            if (CollectionUtils.isEmpty(infos)) {
                R<Object> error = R.error(ResponseCode.NO_RESPONSE_DATA);
                //将error转化成json格式字符串
                String jsonData = new ObjectMapper().writeValueAsString(error);
                //设置响应的数据格式 告知浏览器传入的数据格式
                response.setContentType("application/json");
                //设置编码格式
                //            response.setCharacterEncoding("utf-8");
                //响应数据
                response.getWriter().write(jsonData);
                return;
            }
            //3.调动EasyExcel数据导出
            // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
            response.setContentType("application/vnd.ms-excel");
//        response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("股票涨幅数据表格", "UTF-8");
            //指定excel导出时默认的文件名称，说白了就是告诉浏览器下载文件时默认的名称为：股票涨幅数据表格
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), StockUpdownDomain.class).sheet("股票涨幅信息").doWrite(infos);
        } catch (Exception e) {
            log.error("导出时间：{},当初页码：{}，导出数据量：{}，发生异常信息：{}", lastDate, page, pageSize, e.getMessage());
        }
    }

    /**
     * 功能描述：统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     * map结构示例：
     * {
     * "volList": [{"count": 3926392,"time": "202112310930"},......],
     * "yesVolList":[{"count": 3926392,"time": "202112310930"},......]
     * }
     *
     * @return
     */
    @Override
    public R<Map> stockTradeVol4InnerMarket() {
        //1.获取T日和T-1日的开始时间和结束时间
        //1.1 获取最近股票有效交易时间点--T日时间范围
        DateTime lastDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        DateTime openDateTime = DateTimeUtil.getOpenDate(lastDateTime);
        //转化成java中Date,这样jdbc默认识别
        Date startTime4T = openDateTime.toDate();
        Date endTime4T = lastDateTime.toDate();

        startTime4T = DateTime.parse("2022-01-03 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        endTime4T = DateTime.parse("2022-01-03 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        //1.2 获取T-1日的区间范围
        //获取lastDateTime的上一个股票有效交易日
        DateTime preLastDateTime = DateTimeUtil.getPreviousTradingDay(lastDateTime);
        DateTime preOpenDateTime = DateTimeUtil.getOpenDate(preLastDateTime);
        //转化成java中Date,这样jdbc默认识别
        Date startTime4PreT = preOpenDateTime.toDate();
        Date endTime4PreT = preLastDateTime.toDate();

        startTime4PreT = DateTime.parse("2022-01-02 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        endTime4PreT = DateTime.parse("2022-01-02 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        //2.获取上证和深证的配置的大盘id
        //2.1 获取大盘的id集合
        List<String> markedIds = stockInfoConfig.getInner();
        //3.分别查询T日和T-1日的交易量数据，得到两个集合
        //3.1 查询T日大盘交易统计数据
        List<Map> data4T = stockMarketIndexInfoMapper.getStockTradeVol(markedIds, startTime4T, endTime4T);
        if (CollectionUtils.isEmpty(data4T)) {
            data4T = new ArrayList<>();
        }
        //3.2 查询T-1日大盘交易统计数据
        List<Map> data4PreT = stockMarketIndexInfoMapper.getStockTradeVol(markedIds, startTime4PreT, endTime4PreT);
        if (CollectionUtils.isEmpty(data4PreT)) {
            data4PreT = new ArrayList<>();
        }
        //4.组装响应数据
        HashMap<String, List> info = new HashMap<>();
        info.put("amtList", data4T);
        info.put("yesAmtList", data4PreT);
        //5.返回数据
        return R.ok(info);
    }

    /**
     * 功能描述：统计在当前时间下（精确到分钟），股票在各个涨跌区间的数量
     * 如果当前不在股票有效时间内，则以最近的一个有效股票交易时间作为查询时间点；
     *
     * @return 响应数据格式：
     * {
     * "code": 1,
     * "data": {
     * "time": "2021-12-31 14:58:00",
     * "infos": [
     * {
     * "count": 17,
     * "title": "-3~0%"
     * },
     * //...
     * ]
     * }
     * }
     */
    @Override
    public R<Map> stockUpDownScopeCount() {
        //1.获取股票最新一次交易的时间点
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //mock data
        curDate = DateTime.parse("2022-01-06 09:55:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //2.查询股票信息
        List<Map> maps = stockRtInfoMapper.getStockUpDownSectionByTime(curDate);
        //2.1 获取有序的标题集合
        List<String> orderSections = stockInfoConfig.getUpDownRange();
        List<Map> orderMaps = orderSections.stream().map(title -> {
            Map mp = null;
            Optional<Map> op = maps.stream().filter(m -> m.containsValue(title)).findFirst();
            //判断是否存在符合过滤条件的元素
            if (op.isPresent()) {
                mp = op.get();
            } else {
                mp = new HashMap();
                mp.put("count", 0);
                mp.put("title", title);
            }
            return mp;
        }).collect(Collectors.toList());
        //3.组装数据
        HashMap<String, Object> mapInfo = new HashMap<>();
        //获取指定日期格式的字符串
        String curDateStr = new DateTime(curDate).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        mapInfo.put("time", curDateStr);
        mapInfo.put("infos", orderMaps);
        //4.返回数据
        return R.ok(mapInfo);
    }

    /**
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
     * 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     *
     * @param code 股票编码
     * @return
     */
    @Override
    public R<List<Stock4MinuteDomain>> stockScreenTimeSharing(String code) {
        //1.获取最近最新的交易时间点和对应的开盘日期
        //1.1 获取最近有效时间点
        DateTime lastDate4Stock = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date endTime = lastDate4Stock.toDate();
        endTime = DateTime.parse("2021-12-30 14:47:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        //1.2 获取最近有效时间点对应的开盘日期
        DateTime openDateTime = DateTimeUtil.getOpenDate(lastDate4Stock);
        Date startTime = openDateTime.toDate();
        startTime = DateTime.parse("2021-12-30 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //2.根据股票code和日期范围查询
        List<Stock4MinuteDomain> list = stockRtInfoMapper.getStockInfoByCodeAndDate(code, startTime, endTime);
        //判断非空处理
        if (CollectionUtils.isEmpty(list)) {
            list = new ArrayList<>();
        }
        //3.返回响应数据
        return R.ok(list);
    }

    /**
     * 功能描述：单个个股日K数据查询 ，可以根据时间区间查询数日的K线数据
     * 默认查询历史20天的数据；
     *
     * @param code 股票编码
     * @return
     */
    @Override
    public R<List<Stock4EvrDayDomain>> stockCreenDkLine(String code) {
        //1.获取查询的日期范围
        //1.1 获取截止时间
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date endTime = endDateTime.toDate();
        //TODO MOCKDATA
        endTime = DateTime.parse("2022-01-07 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //1.2 获取开始时间
        DateTime startDateTime = endDateTime.minusDays(10);
        Date startTime = startDateTime.toDate();
        //TODO MOCKDATA
        startTime = DateTime.parse("2022-01-01 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //2.调用mapper接口获取查询的集合信息-方案1
        List<Stock4EvrDayDomain> data = stockRtInfoMapper.getStockInfo4EvrDay(code, startTime, endTime);
        //3.组装数据，响应
        return R.ok(data);
    }

    /**
     * 定义获取A股大盘最新数据
     *
     * @return
     */
    @Override
    public R<List<InnerMarketDomain>> getInnnerMarketInfos() {
        //从缓存中加载数据，如果不存在，则走补偿策略获取数据，并存入本地缓存
        R<List<InnerMarketDomain>> data = (R<List<InnerMarketDomain>>) caffeineCache.get("innerMarketInfos", key -> {
            //如果不存在，则从数据库查询
            //1.获取最新的股票交易时间点
            Date lastDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
            //TODO 伪造数据，后续删除
            lastDate = DateTime.parse("2022-01-03 09:47:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            //2.获取国内大盘编码集合
            List<String> innerCodes = stockInfoConfig.getInner();
            //3.调用mapper查询
            List<InnerMarketDomain> infos = stockMarketIndexInfoMapper.getInnerIndexByTimeAndCodes(lastDate, innerCodes);
            //4.响应
            return R.ok(infos);
        });
        return data;
    }

    @Override
    public void getNewestInnerMarketInfos() {

    }
}