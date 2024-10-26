package top.atluofu.stock.vo;

import lombok.Data;

/**
 * @ClassName: StockApp
 * @description: 登录请求vo
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-26Day-12:48
 * @Version: 1.0
 */
@Data
public class LoginReqVo {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 验证码
     */
    private String code;

    /**
     * 存入redis的随机码的key
     */
    //private String sessionId;
}
