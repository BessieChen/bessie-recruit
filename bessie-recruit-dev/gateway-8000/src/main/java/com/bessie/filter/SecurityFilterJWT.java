package com.bessie.filter;

import com.bessie.base.BaseInfoProperties;
import com.bessie.grace.result.GraceJsonResult;
import com.bessie.grace.result.ResponseStatusEnum;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
public class SecurityFilterJWT extends BaseInfoProperties implements GlobalFilter, Ordered {

    @Autowired
    private ExcludeUrlProperties excludeUrlProperties;

    // 路径匹配的规则器
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 1. 获取当前的请求路径
        String url = exchange.getRequest().getURI().getPath();

        // 2. 获得所有的需要排除校验的url list
        List<String> excludeList = excludeUrlProperties.getUrls();

        // 3. 校验并且排除excludeList
        if (excludeList != null && !excludeList.isEmpty()) {
            for (String excludeUrl : excludeList) {
                if (antPathMatcher.matchStart(excludeUrl, url)) { //使用路由匹配来做判断
                    log.warn("放行: SecurityFilterJWT url=" + url);
                    // 如果匹配到，则直接放行，表示当前的请求url是不需要被拦截校验的
                    return chain.filter(exchange);
                }
            }
        }

        // 针对指定的url，对ip进行判断拦截，限制访问次数
        // 到达此处表示被拦截
        log.warn("被拦截: SecurityFilterJWT url=" + url);

        //  无法触发全局异常拦截处理，需要手动返回
        //GraceException.doException(ResponseStatusEnum.UN_LOGIN);
        //return chain.filter(exchange);

        // 默认不放行，没有token则返回错误，到达这里的都是漏掉的没有在ExcludeUrlPath中配置
        return renderErrorMsg(exchange, ResponseStatusEnum.UN_LOGIN);
    }

    /**
     * 包装并且返回错误信息
     * @param exchange
     * @param statusEnum
     * @return
     */
    public Mono<Void> renderErrorMsg(ServerWebExchange exchange,
                                     ResponseStatusEnum statusEnum) {
        // 1. 获得response
        ServerHttpResponse httpResponse = exchange.getResponse();

        // 2. 构建jsonResult, 这个就是我们最终要返回的 json 对象{枚举类 UN_LOGIN}
        GraceJsonResult graceJSONResult = GraceJsonResult.exception(statusEnum);

        // 3. 修改code为{错误码500}
        httpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        // 4. 设定返回json类型
        if (!httpResponse.getHeaders().containsKey("Content-Type")) {
            httpResponse.getHeaders().add("Content-Type", "application/json");
        }

        // 5. 转换json字符串，并且向response中写入数据
        String resultJson = new Gson().toJson(graceJSONResult);
        DataBuffer buffer = httpResponse
                .bufferFactory()
                .wrap(resultJson.getBytes(StandardCharsets.UTF_8));

        return httpResponse.writeWith(Mono.just(buffer));
    }




    // 过滤器的顺序，数字越小优先级则越大
    @Override
    public int getOrder() {
        return 0;
    }
}
