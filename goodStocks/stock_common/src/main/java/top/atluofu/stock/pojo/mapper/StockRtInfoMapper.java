package top.atluofu.stock.pojo.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import top.atluofu.stock.pojo.domain.Stock4EvrDayDomain;
import top.atluofu.stock.pojo.domain.Stock4MinuteDomain;
import top.atluofu.stock.pojo.domain.StockUpdownDomain;
import top.atluofu.stock.pojo.entity.StockRtInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author MQa010225
 * @description 针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper
 * @createDate 2024-10-26 12:43:43
 * @Entity top.atluofu.stick.pojo.entity.StockRtInfo
 */
public interface StockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);

    List<StockUpdownDomain> getNewestStockInfo(Date curDate);

    @MapKey("time")
    List<Map> getStockUpdownCount(@Param("openTime") Date openTime, @Param("curTime") Date curTime, @Param("flag") int flag);

    List<StockUpdownDomain> getStockUpDownInfos(@Param("timePoint") Date timePoint);

    @MapKey("title")
    List<Map> stockUpDownScopeCount(@Param("avlDate") Date avlDate);

    @MapKey("title")
    List<Map> getStockUpDownSectionByTime(@Param("curDate") Date curDate);

    List<Stock4MinuteDomain> getStockInfoByCodeAndDate(@Param("stockCode") String stockCode,
                                                       @Param("startTime") Date startTime,
                                                       @Param("endTime") Date endTime);

    List<Stock4EvrDayDomain> getStockInfo4EvrDay(@Param("stockCode") String stockCode,
                                                 @Param("startTime") Date startTime,
                                                 @Param("endTime") Date endTime);
}
