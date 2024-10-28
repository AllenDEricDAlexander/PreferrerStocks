package top.atluofu.stock.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.atluofu.stock.constant.StockConstant;
import top.atluofu.stock.pojo.entity.SysUser;
import top.atluofu.stock.pojo.mapper.SysUserMapper;
import top.atluofu.stock.service.UserService;
import top.atluofu.stock.utils.IdWorker;
import top.atluofu.stock.vo.LoginReqVo;
import top.atluofu.stock.vo.LoginRespVo;
import top.atluofu.stock.vo.R;
import top.atluofu.stock.vo.ResponseCode;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: UserServiceImpl
 * @description: 定义服务接口实现
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-26Day-12:51
 * @Version: 1.0
 */
@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {

    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate redisTemplate;
    private final IdWorker idWorker;

    public UserServiceImpl(SysUserMapper sysUserMapper, PasswordEncoder passwordEncoder, RedisTemplate redisTemplate, IdWorker idWorker) {
        this.sysUserMapper = sysUserMapper;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
        this.idWorker = idWorker;
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
        //1.校验参数的合法性
        if (vo == null || StringUtils.isBlank(vo.getUsername()) || StringUtils.isBlank(vo.getPassword())) {
            return R.error(ResponseCode.DATA_ERROR);
        }
        //2.校验验证码和sessionId是否有效
        if (StringUtils.isBlank(vo.getCode()) || StringUtils.isBlank(vo.getSessionId())) {
            return R.error(ResponseCode.DATA_ERROR);
        }
        //3.根据Rkey从redis中获取缓存的校验码
        String rCode = (String) redisTemplate.opsForValue().get(StockConstant.CHECK_PREFIX + vo.getSessionId());
        //判断获取的验证码是否存在，以及是否与输入的验证码相同
        if (StringUtils.isBlank(rCode) || !rCode.equalsIgnoreCase(vo.getCode())) {
            //验证码输入有误
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }
        //4.根据账 户名称去数据库查询获取用户信息
        SysUser dbUser = sysUserMapper.getUserByUserName(vo.getUsername());
        //5.判断数据库用户是否存在
        if (dbUser == null) {
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }
        //6.如果存在，则获取密文密码，然后传入的明文进行匹配,判断是否匹配成功
        if (!passwordEncoder.matches(vo.getPassword(), dbUser.getPassword())) {
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }
        //7.正常响应
        LoginRespVo respVo = new LoginRespVo();
        BeanUtils.copyProperties(dbUser, respVo);
        return R.ok(respVo);
    }

    /**
     * 登录校验吗方法实现
     *
     * @return
     */
    @Override
    public R<Map> getCaptchaCode() {
        //参数分别是宽、高、验证码长度、干扰线数量
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(250, 40, 4, 5);
        //设置背景颜色清灰
        captcha.setBackground(Color.lightGray);
        //自定义校验码生成方式
//        captcha.setGenerator(new CodeGenerator() {
//            @Override
//            public String generate() {
//                return RandomStringUtils.randomNumeric(4);
//            }
//            @Override
//            public boolean verify(String code, String userInputCode) {
//                return code.equalsIgnoreCase(userInputCode);
//            }
//        });
        //获取图片中的验证码，默认生成的校验码包含文字和数字，长度为4
        String checkCode = captcha.getCode();
        log.info("生成校验码:{}", checkCode);
        //生成sessionId
        String sessionId = String.valueOf(idWorker.nextId());
        //将sessionId和校验码保存在redis下，并设置缓存中数据存活时间一分钟
        redisTemplate.opsForValue().set(StockConstant.CHECK_PREFIX + sessionId, checkCode, 1, TimeUnit.MINUTES);
        //组装响应数据
        HashMap<String, String> info = new HashMap<>();
        info.put("sessionId", sessionId);
        //获取base64格式的图片数据
        info.put("imageData", captcha.getImageBase64());
        //设置响应数据格式
        return R.ok(info);
    }
}
