package top.atluofu.stock.serrvice.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import top.atluofu.stock.constant.ParseType;
import top.atluofu.stock.pojo.entity.StockMarketIndexInfo;
import top.atluofu.stock.pojo.entity.StockRtInfo;
import top.atluofu.stock.pojo.mapper.StockBusinessMapper;
import top.atluofu.stock.pojo.mapper.StockMarketIndexInfoMapper;
import top.atluofu.stock.pojo.mapper.StockRtInfoMapper;
import top.atluofu.stock.serrvice.StockTimerTaskService;
import top.atluofu.stock.utils.DateTimeUtil;
import top.atluofu.stock.utils.IdWorker;
import top.atluofu.stock.utils.ParserStockInfoUtil;
import top.atluofu.stock.vo.StockInfoConfig;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @ClassName: StockTimerTaskServiceImpl
 * @description: 定义采集股票数据的定时任务的服务接口
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-28Day-10:46
 * @Version: 1.0
 */
@Service("stockTimerTaskService")
@Slf4j
public class StockTimerTaskServiceImpl implements StockTimerTaskService {

    //注入格式解析bean
    private final ParserStockInfoUtil parserStockInfoUtil;

    private final StockBusinessMapper stockBusinessMapper;

    private final StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    private final RestTemplate restTemplate;

    private final StockInfoConfig stockInfoConfig;

    private final IdWorker idWorker;

    private final RabbitTemplate rabbitTemplate;

    private final StockRtInfoMapper stockRtInfoMapper;

    public StockTimerTaskServiceImpl(RestTemplate restTemplate, StockInfoConfig stockInfoConfig, IdWorker idWorker, StockMarketIndexInfoMapper stockMarketIndexInfoMapper, ParserStockInfoUtil parserStockInfoUtil, StockBusinessMapper stockBusinessMapper, RabbitTemplate rabbitTemplate, StockRtInfoMapper stockRtInfoMapper) {
        this.restTemplate = restTemplate;
        this.stockInfoConfig = stockInfoConfig;
        this.idWorker = idWorker;
        this.stockMarketIndexInfoMapper = stockMarketIndexInfoMapper;
        this.parserStockInfoUtil = parserStockInfoUtil;
        this.stockBusinessMapper = stockBusinessMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.stockRtInfoMapper = stockRtInfoMapper;
    }

    @Override
    public void getInnerMarketInfo() {
        //1.定义采集的url接口
        String url = stockInfoConfig.getMarketUrl() + String.join(",", stockInfoConfig.getInner());
        //2.调用restTemplate采集数据
        //2.1 组装请求头
        HttpHeaders headers = new HttpHeaders();
        //必须填写，否则数据采集不到
        headers.add("Referer", "https://finance.sina.com.cn/stock/");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        //2.2 组装请求对象
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        //2.3 resetTemplate发起请求
        String resString = restTemplate.postForObject(url, entity, String.class);
        //log.info("当前采集的数据：{}",resString);
        //3.数据解析（重要）
//        var hq_str_sh000001="上证指数,3267.8103,3283.4261,3236.6951,3290.2561,3236.4791,0,0,402626660,398081845473,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2022-04-07,15:01:09,00,";
//        var hq_str_sz399001="深证成指,12101.371,12172.911,11972.023,12205.097,11971.334,0.000,0.000,47857870369,524892592190.995,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,2022-04-07,15:00:03,00";
        String reg = "var hq_str_(.+)=\"(.+)\";";
        //编译表达式,获取编译对象
        Pattern pattern = Pattern.compile(reg);
        //匹配字符串
        Matcher matcher = pattern.matcher(resString);
        ArrayList<StockMarketIndexInfo> list = new ArrayList<>();
        //判断是否有匹配的数值
        while (matcher.find()) {
            //获取大盘的code
            String marketCode = matcher.group(1);
            //获取其它信息，字符串以逗号间隔
            String otherInfo = matcher.group(2);
            //以逗号切割字符串，形成数组
            String[] splitArr = otherInfo.split(",");
            //大盘名称
            String marketName = splitArr[0];
            //获取当前大盘的开盘点数
            BigDecimal openPoint = new BigDecimal(splitArr[1]);
            //前收盘点
            BigDecimal preClosePoint = new BigDecimal(splitArr[2]);
            //获取大盘的当前点数
            BigDecimal curPoint = new BigDecimal(splitArr[3]);
            //获取大盘最高点
            BigDecimal maxPoint = new BigDecimal(splitArr[4]);
            //获取大盘的最低点
            BigDecimal minPoint = new BigDecimal(splitArr[5]);
            //获取成交量
            Long tradeAmt = Long.valueOf(splitArr[8]);
            //获取成交金额
            BigDecimal tradeVol = new BigDecimal(splitArr[9]);
            //时间
            Date curTime = DateTimeUtil.getDateTimeWithoutSecond(splitArr[30] + " " + splitArr[31]).toDate();
            //组装entity对象
            StockMarketIndexInfo info = StockMarketIndexInfo.builder()
                    .id(idWorker.nextId())
                    .marketCode(marketCode)
                    .marketName(marketName)
                    .curPoint(curPoint)
                    .openPoint(openPoint)
                    .preClosePoint(preClosePoint)
                    .maxPoint(maxPoint)
                    .minPoint(minPoint)
                    .tradeVolume(tradeVol)
                    .tradeAmount(tradeAmt)
                    .curTime(curTime)
                    .build();
            //收集封装的对象，方便批量插入
            list.add(info);
        }
        log.info("采集的当前大盘数据：{}", list);
        //批量插入
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        //解析的数据批量插入数据库
        int count = stockMarketIndexInfoMapper.insertBatch(list);
        log.info("当前插入了：{}行数据", count);
        //通知后台终端刷新本地缓存，发送的日期数据是告知对方当前更新的股票数据所在时间点
        rabbitTemplate.convertAndSend("stockExchange", "inner.market", new Date());
    }

    /**
     * 批量获取股票分时数据详情信息
     * http://hq.sinajs.cn/list=sz000002,sh600015
     */
    @Override
    public void getStockRtIndex() {
        //批量获取股票ID集合
        List<String> stockIds = stockBusinessMapper.getStockIds();
        //计算出符合sina命名规范的股票id数据
        stockIds = stockIds.stream().map(id -> {
            return id.startsWith("6") ? "sh" + id : "sz" + id;
        }).collect(Collectors.toList());
        //一次性查询过多，我们将需要查询的数据先进行分片处理，每次最多查询20条股票数据
        Lists.partition(stockIds, 20).forEach(list -> {
            //拼接股票url地址
            String stockUrl = stockInfoConfig.getMarketUrl() + String.join(",", list);
            //获取响应数据
            String result = restTemplate.getForObject(stockUrl, String.class);
            List<StockRtInfo> infos = parserStockInfoUtil.parser4StockOrMarketInfo(result, ParseType.ASHARE);
            log.info("数据量：{}", infos.size());
            //批量插入数据库
            stockRtInfoMapper.insertBatch(infos);
        });
    }
}
