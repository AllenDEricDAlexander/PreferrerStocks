package top.atluofu.stock.pojo.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import top.atluofu.stock.pojo.domain.InnerMarketDomain;
import top.atluofu.stock.pojo.entity.StockMarketIndexInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author MQa010225
 * @description 针对表【stock_market_index_info(国内大盘数据详情表)】的数据库操作Mapper
 * @createDate 2024-10-26 12:43:43
 * @Entity top.atluofu.stick.pojo.entity.StockMarketIndexInfo
 */
public interface StockMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockMarketIndexInfo record);

    int insertSelective(StockMarketIndexInfo record);

    StockMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockMarketIndexInfo record);

    int updateByPrimaryKey(StockMarketIndexInfo record);

    List<InnerMarketDomain> getMarketInfo(@Param("marketIds") List<String> marketIds, @Param("timePoint") Date timePoint);

    @MapKey("time")
    List<Map> getStockTradeVol(@Param("markedIds") List<String> markedIds,
                               @Param("startTime") Date startTime,
                               @Param("endTime") Date endTime);


    int insertBatch(List<StockMarketIndexInfo> infos);

    List<InnerMarketDomain> getInnerIndexByTimeAndCodes(Date lastDate, List<String> innerCodes);
}
