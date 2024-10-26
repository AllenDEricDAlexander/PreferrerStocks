package top.atluofu.stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * @ClassName: UserController
 * @description: 定义用户处理器接口
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-26Day-12:52
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据用户名查询用户信息
     * @param userName
     * @return
     */
    @GetMapping("/user/{userName}")
    public SysUser getUserByUserName(@PathVariable("userName") String userName){
        return userService.getUserByUserName(userName);
    }

    /**
     * 用户登录功能实现
     * @param vo
     * @return
     */
    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo vo){
        return this.userService.login(vo);
    }
}