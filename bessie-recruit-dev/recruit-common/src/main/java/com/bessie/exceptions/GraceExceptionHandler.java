package com.bessie.exceptions;

import com.bessie.grace.result.GraceJsonResult;
import com.bessie.grace.result.ResponseStatusEnum;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.security.SignatureException;
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

//    @ExceptionHandler(ArithmeticException.class)
//    @ResponseBody
//    public GraceJsonResult returnArithmeticException(ArithmeticException e) {
//        e.printStackTrace();
//        return GraceJsonResult.errorMsg(e.getMessage());
//    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public GraceJsonResult returnMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        e.printStackTrace();
        return GraceJsonResult.exception(ResponseStatusEnum.FILE_MAX_SIZE_500KB_ERROR);
    }

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

    @ExceptionHandler({
            SignatureException.class,
            ExpiredJwtException.class,
            UnsupportedJwtException.class,
            MalformedJwtException.class,
            io.jsonwebtoken.security.SignatureException.class
    })
    @ResponseBody
    public GraceJsonResult returnSignatureException(SignatureException e) {
        e.printStackTrace();
        return GraceJsonResult.exception(ResponseStatusEnum.JWT_SIGNATURE_ERROR);
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