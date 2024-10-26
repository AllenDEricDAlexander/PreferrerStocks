package top.atluofu.stock.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.atluofu.stock.pojo.entity.SysUser;
import top.atluofu.stock.pojo.mapper.SysUserMapper;
import top.atluofu.stock.service.UserService;
import top.atluofu.stock.vo.LoginReqVo;
import top.atluofu.stock.vo.LoginRespVo;
import top.atluofu.stock.vo.R;
import top.atluofu.stock.vo.ResponseCode;

/**
 * @ClassName: UserServiceImpl
 * @description: 定义服务接口实现
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-26Day-12:51
 * @Version: 1.0
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(SysUserMapper sysUserMapper, PasswordEncoder passwordEncoder) {
        this.sysUserMapper = sysUserMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 根据用户名称查询用户信息
     *
     * @param userName 用户名称
     * @return
     */
    @Override
    public SysUser getUserByUserName(String userName) {
        return sysUserMapper.findByUserName(userName);
    }

    @Override
    public R<LoginRespVo> login(LoginReqVo vo) {
        if (vo == null || StringUtils.isBlank(vo.getUsername()) || StringUtils.isBlank(vo.getPassword())) {
            return R.error(ResponseCode.DATA_ERROR.getMessage());
        }
        //根据用户名查询用户信息
        SysUser user = this.sysUserMapper.findByUserName(vo.getUsername());
        //判断用户是否存在，存在则密码校验比对
        if (user == null || !passwordEncoder.matches(vo.getPassword(), user.getPassword())) {
            return R.error(ResponseCode.SYSTEM_PASSWORD_ERROR.getMessage());
        }
        //组装登录成功数据
        LoginRespVo respVo = new LoginRespVo();
        //属性名称与类型必须相同，否则属性值无法copy
        BeanUtils.copyProperties(user, respVo);
        return R.ok(respVo);
    }
}
