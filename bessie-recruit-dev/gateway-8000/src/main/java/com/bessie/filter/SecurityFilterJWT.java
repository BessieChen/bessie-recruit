package com.bessie.filter;

import com.bessie.base.BaseInfoProperties;
import com.bessie.grace.result.GraceJsonResult;
import com.bessie.grace.result.ResponseStatusEnum;
import com.bessie.utils.JWTUtils;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
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

    public static final String HEADER_USER_TOKEN = "headerUserToken";

    @Autowired
    private ExcludeUrlProperties excludeUrlProperties;

    @Autowired
    private JWTUtils jwtUtils;

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

        // 判断header中是否有token，对用户请求进行判断过滤
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String userToken = headers.getFirst(HEADER_USER_TOKEN);
        //不要自定义"headerUserToken", 和前端联调就是这个名字, 否则无法获得 jwt

        //header 判空token
        //bes: 所以我们的逻辑是, 只要有一个条件允许放行那就放行, 什么条件都没有通过才是拦截. 所以更像是"默认你是好人,无罪假定"
        if (StringUtils.isNotBlank(userToken)) {
            // 分割判断token来源（app/saas/admin）
            String[] tokenArr = userToken.split("@");
            if (tokenArr.length < 2) {
                return renderErrorMsg(exchange, ResponseStatusEnum.UN_LOGIN);
            }
            // 获得前缀与jwt令牌
            String prefix = tokenArr[0];
            String jwt = tokenArr[1];

            return dealJWT(jwt, exchange, chain);
        }

        // 默认不放行，没有token则返回错误，到达这里的都是漏掉的没有在ExcludeUrlPath中配置
        return renderErrorMsg(exchange, ResponseStatusEnum.UN_LOGIN);
    }

    public Mono<Void> dealJWT(String jwt, ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            String userJson = jwtUtils.checkJWT(jwt);
            //checkJWT()若出现异常, 这个异常的拦截, 上节说了, 我们不能用全局的统一异常去处理的. 所以需要手动拦截 checkJWT()的异常
            //所以就直接使用 try{} catch(){}
            //快捷键: alt + cmd: 选择 try+catch
            log.info("JWT校验完毕，userJson = " + userJson);
            return chain.filter(exchange); //既然没有报异常, 那就是jwt校验通过了, 放行
        } catch (ExpiredJwtException e) { //对应"过期"的exception, 是官方实现的
            e.printStackTrace();
            return renderErrorMsg(exchange, ResponseStatusEnum.JWT_EXPIRE_ERROR); //jwt过期了
        } catch (Exception e) { //更大范围的 exception
            e.printStackTrace();
            return renderErrorMsg(exchange, ResponseStatusEnum.JWT_SIGNATURE_ERROR);
        }
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
