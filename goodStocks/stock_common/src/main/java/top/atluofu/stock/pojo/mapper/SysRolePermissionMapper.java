package top.atluofu.stock.pojo.mapper;

import top.atluofu.stock.pojo.entity.SysRolePermission;

/**
* @author MQa010225
* @description 针对表【sys_role_permission(角色权限表)】的数据库操作Mapper
* @createDate 2024-10-26 12:43:43
* @Entity top.atluofu.stick.pojo.entity.SysRolePermission
*/
public interface SysRolePermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    SysRolePermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRolePermission record);

    int updateByPrimaryKey(SysRolePermission record);

}
