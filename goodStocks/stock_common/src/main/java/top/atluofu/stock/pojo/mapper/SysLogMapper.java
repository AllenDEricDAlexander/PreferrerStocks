package top.atluofu.stock.pojo.mapper;

import top.atluofu.stock.pojo.entity.SysLog;

/**
* @author MQa010225
* @description 针对表【sys_log(系统日志)】的数据库操作Mapper
* @createDate 2024-10-26 12:43:43
* @Entity top.atluofu.stick.pojo.entity.SysLog
*/
public interface SysLogMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);

}
