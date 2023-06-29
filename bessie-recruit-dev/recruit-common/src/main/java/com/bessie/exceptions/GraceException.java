package com.bessie.exceptions;

import com.bessie.grace.result.ResponseStatusEnum;

/**
 * @program: bessie-recruit-dev
 * @description: 优雅的处理异常，统一进行封装
 * @author: Bessie
 * @create: 2023-06-28 19:23
 **/
public class GraceException {

    public static void doException(ResponseStatusEnum statusEnum) {
        throw new MyCustomException(statusEnum);
    }

}
