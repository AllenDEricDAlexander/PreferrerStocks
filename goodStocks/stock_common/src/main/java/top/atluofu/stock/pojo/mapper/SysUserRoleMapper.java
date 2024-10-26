package top.atluofu.stock.pojo.mapper;

import top.atluofu.stock.pojo.entity.SysUserRole;

/**
* @author MQa010225
* @description 针对表【sys_user_role(用户角色表)】的数据库操作Mapper
* @createDate 2024-10-26 12:43:44
* @Entity top.atluofu.stick.pojo.entity.SysUserRole
*/
public interface SysUserRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

}
