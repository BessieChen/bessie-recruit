package com.bessie.api.intercept;

import com.bessie.base.BaseInfoProperties;
import com.bessie.exceptions.GraceException;
import com.bessie.exceptions.MyCustomException;
import com.bessie.grace.result.ResponseStatusEnum;
import com.bessie.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: bessie-recruit-dev
 * @description:
 * @author: Bessie
 * @create: 2023-06-28 16:42
 **/
@Slf4j
public class SMSInterceptor extends BaseInfoProperties implements HandlerInterceptor {
    /**
     * 拦截请求，访问controller之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获得用户ip
        String userIp = IPUtil.getRequestIp(request);
        boolean keyIsExist = redis.keyIsExist(MOBILE_SMSCODE + ":" + userIp);
        if (keyIsExist) {
            log.error("短信发送频率太大！");
            GraceException.doException(ResponseStatusEnum.SMS_NEED_WAIT_ERROR); //SMS_NEED_WAIT_ERROR(505,false,"短信发送太快啦~请稍后再试！"),
            return false;
        }

        /**
         * false：请求被拦截
         * true：请求通过验证，放行
         */
        return true;
    }


    /**
     * 请求访问到controller之后，渲染视图之前
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 请求访问到controller之后，渲染视图之后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
