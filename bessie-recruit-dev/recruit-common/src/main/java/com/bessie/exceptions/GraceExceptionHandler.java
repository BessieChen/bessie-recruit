package com.bessie.exceptions;

import com.bessie.grace.result.GraceJsonResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
public class GraceExceptionHandler {

    @ExceptionHandler(MyCustomException.class)
    @ResponseBody
    public GraceJsonResult returnMyCustomException(MyCustomException e) {
        e.printStackTrace();
        return GraceJsonResult.exception(e.getResponseStatusEnum());
    }

    public Map<String, String> getErrors(BindingResult result) {

        Map<String, String> map = new HashMap<>();

        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError fe : errorList) {
            // 错误所对应的属性字段名
            String field = fe.getField();
            // 错误信息
            String message = fe.getDefaultMessage();

            map.put(field, message);
        }

        return map;
    }

}