package top.atluofu.stock.pojo.mapper;

import org.apache.ibatis.annotations.Param;
import top.atluofu.stock.pojo.entity.SysUser;

/**
 * @author MQa010225
 * @description 针对表【sys_user(用户表)】的数据库操作Mapper
 * @createDate 2024-10-26 12:43:43
 * @Entity top.atluofu.stick.pojo.entity.SysUser
 */
public interface SysUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    /**
     * 根据用户名称查询用户信息
     *
     * @param userName 用户名称
     * @return SysUser
     */
    SysUser findByUserName(@Param("name") String userName);
}
