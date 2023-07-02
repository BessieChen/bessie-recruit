package com.bessie.filter;

import com.bessie.base.BaseInfoProperties;
import com.bessie.exceptions.GraceException;
import com.bessie.grace.result.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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
        GraceException.doException(ResponseStatusEnum.UN_LOGIN);
        return chain.filter(exchange);
    }




    // 过滤器的顺序，数字越小优先级则越大
    @Override
    public int getOrder() {
        return 0;
    }
}
