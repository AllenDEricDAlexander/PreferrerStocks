package top.atluofu.stock.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.atluofu.stock.pojo.entity.SysUser;
import top.atluofu.stock.service.UserService;
import top.atluofu.stock.vo.LoginReqVo;
import top.atluofu.stock.vo.LoginRespVo;
import top.atluofu.stock.vo.R;

import java.util.Map;

/**
 * @ClassName: UserController
 * @description: 定义用户处理器接口
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-26Day-12:52
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api")
@Api(value = "用户认证相关接口定义", tags = "用户功能-用户登录功能")
public class UserController {
    /**
     * 注入用户服务bean
     */
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param userName
     * @return SysUser
     */
    @GetMapping("/{userName}")
    @ApiOperation(value = "根据用户名查询用户信息", notes = "用户信息查询", response = SysUser.class)
    @ApiImplicitParam(paramType = "path", name = "userName", value = "用户名", required = true)
    public SysUser getUserByUserName(@PathVariable("userName") String userName) {
        return userService.getUserByUserName(userName);
    }

    /**
     * 用户登录功能接口
     *
     * @param vo
     * @return R<LoginRespVo>
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录功能", notes = "用户登录", response = R.class)
    public R<LoginRespVo> login(@RequestBody LoginReqVo vo) {
        return userService.login(vo);
    }

    /**
     * 生成登录校验码的访问接口
     *
     * @return R<Map>
     */
    @GetMapping("/captcha")
    @ApiOperation(value = "验证码生成功能", response = R.class)
    public R<Map> getCaptchaCode() {
        return userService.getCaptchaCode();
    }
}