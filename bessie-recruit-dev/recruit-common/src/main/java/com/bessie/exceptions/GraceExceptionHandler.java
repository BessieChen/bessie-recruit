package com.bessie.exceptions;

import com.bessie.grace.result.GraceJsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: bessie-recruit-dev
 * @description:
 *  * 统一异常拦截处理
 *  * 可以针对异常的类型进行捕获，然后返回json信息到前端
 * @author: Bessie
 * @create: 2023-06-28 19:23
 **/
@ControllerAdvice
@Slf4j
public class GraceExceptionHandler {

    @ExceptionHandler(MyCustomException.class)
    @ResponseBody
    public GraceJsonResult returnMyCustomException(MyCustomException e) {
        e.printStackTrace();
        return GraceJsonResult.exception(e.getResponseStatusEnum());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public GraceJsonResult returnMethodArgumentNotValid(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult(); //BindingResult 可以获得错误因袭
        Map<String, String> map = getErrors(result); //getError()实现见下.

        log.error("show?");

        return GraceJsonResult.errorMap(map);
    }

    public Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError ff : errorList) {
            String field = ff.getField(); // 错误所对应的属性字段名
            String msg = ff.getDefaultMessage(); // 错误的信息
            map.put(field, msg);
        }
        return map;
    }

}