package top.atluofu.stock.service;

import top.atluofu.stock.pojo.entity.SysUser;
import top.atluofu.stock.vo.LoginReqVo;
import top.atluofu.stock.vo.LoginRespVo;
import top.atluofu.stock.vo.R;

/**
 * @ClassName: UserService
 * @description: 定义用户服务接口
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-26Day-12:50
 * @Version: 1.0
 */
public interface UserService {
    /**
     * 根据用户查询用户信息
     *
     * @param userName 用户名称
     * @return SysUser
     */
    SysUser getUserByUserName(String userName);

    /**
     * 用户登录功能实现
     * @param vo
     * @return  R<LoginRespVo>
     */
    R<LoginRespVo> login(LoginReqVo vo);
}
