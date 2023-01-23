package com.young.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.young.pojo.User;
import com.young.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Objects;

/**
 * jwt工具类，用于生成token
 */

@Component
public class TokenUtils {
    private static UserService staticUserService;
    @Resource
    private UserService userService;

    @PostConstruct
    public void setUserService() {
        staticUserService = userService;
    }


    /**
     * 生成token的方法
     *
     * @param userId 用户名，作为token的载荷
     * @param sign   密码，作为token的密钥
     * @return 返回生成的token
     */
    public static String genToken(String userId, String sign) {
        return JWT.create().withAudience(userId)           //将userId存入token作为载荷
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))      //使用hutool中的工具类设置过期事件，第一个参数传入当前时间，第二个参数传入要延迟的时间。这里是两小时后token过期
                .sign(Algorithm.HMAC256(sign));     //将密码sign作为token的密钥
    }


    /**
     * 获取当前登录对象的用户信息
     *
     * @return 返回用户的User类
     */
    public static User getCurrentUser() {
        try {
            //先获取token
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String token = request.getHeader("token");
            //判空
            if (StrUtil.isNotBlank(token)) {

                //通过token获取id，在通过向service中传入id来获取user并返回
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserService.getById(Integer.valueOf(userId));

            } else {
                //如果token为空返回空对象
                return null;
            }
        } catch (Exception e) {
            //如果出现异常就返回空对象
            return null;
        }
    }
}
