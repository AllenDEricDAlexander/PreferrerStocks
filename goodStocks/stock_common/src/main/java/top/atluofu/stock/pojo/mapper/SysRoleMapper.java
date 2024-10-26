package top.atluofu.stock.pojo.mapper;

import top.atluofu.stock.pojo.entity.SysRole;

/**
* @author MQa010225
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2024-10-26 12:43:43
* @Entity top.atluofu.stick.pojo.entity.SysRole
*/
public interface SysRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

}
