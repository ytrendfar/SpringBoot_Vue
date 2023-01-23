package com.young.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.young.common.Constants;
import com.young.exception.ServiceException;
import com.young.pojo.User;
import com.young.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt拦截器
 */

public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //获取前端发送请求时携带的token
        String token = request.getHeader("token");
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) return true;
        //执行认证，通过hutool来检测token是否是空字符串
        if (StrUtil.isBlank(token)) throw new ServiceException(Constants.CODE_401, "无token，请重新登录");
        //获取token中存储的用户id，来检测是否合法
        String userId;
        try {
            //通过JWT的decode方法来通过token字符串来获取信息。通过getAudience获取载荷，并通过传入索引的方式获取指定的属性值
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException e) {
            throw new ServiceException(Constants.CODE_401, "token验证失败");
        }
        //通过service中的方法，传入token中的id来获取user对象，查看是否合法
        User user = userService.getById(userId);
        if (user == null) throw new ServiceException(Constants.CODE_401, "用户不存在，请重新登录");
        //验证token
        //给verifier加签上已获取的user的密码
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            //进行验证
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录");
        }
        //前面都通过的话就返回true
        return true;
    }
}
