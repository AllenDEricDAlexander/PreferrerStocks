package top.atluofu.stock.pojo.mapper;

import org.apache.ibatis.annotations.Param;
import top.atluofu.stock.pojo.domain.StockBlockDomain;
import top.atluofu.stock.pojo.entity.StockBlockRtInfo;
import top.atluofu.stock.pojo.entity.StockRtInfo;

import java.util.Date;
import java.util.List;

/**
 * @author MQa010225
 * @description 针对表【stock_block_rt_info(股票板块详情信息表)】的数据库操作Mapper
 * @createDate 2024-10-26 12:43:43
 * @Entity top.atluofu.stick.pojo.entity.StockBlockRtInfo
 */
public interface StockBlockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBlockRtInfo record);

    int insertSelective(StockBlockRtInfo record);

    StockBlockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBlockRtInfo record);

    int updateByPrimaryKey(StockBlockRtInfo record);

    List<StockBlockDomain> sectorAllLimit(@Param("timePoint") Date timePoint);
}
