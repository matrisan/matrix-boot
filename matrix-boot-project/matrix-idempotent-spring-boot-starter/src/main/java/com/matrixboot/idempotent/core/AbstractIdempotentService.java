package com.matrixboot.idempotent.core;

import cn.hutool.core.net.NetUtil;
import com.matrixboot.idempotent.config.IdempotentProperties;
import com.matrixboot.idempotent.exception.GenerateTokenIdempotentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 幂等服务核心类
 * <p>
 * create in 2022/1/24 3:19 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Slf4j
public abstract class AbstractIdempotentService {

    private static final String HEAD_KEY = "Idempotent";

    @Resource
    private IdempotentProperties idempotentProperties;

    @Resource
    private HttpServletRequest request;

    /**
     * 获取最终的 token
     * 前缀 + 自动生成的
     *
     * @return String
     */
    public String generateToken() {
        for (int i = 0; i < idempotentProperties.getRetry(); i++) {
            String tempToken = getSubToken();
            String finalToken = idempotentProperties.getRedisKeyPrefix() + tempToken;
            String value = generalValue();
            boolean persistence = persistence(finalToken, value);
            if (persistence) {
                log.debug("生成 token 成功, 并存入了 redis - {} - {}", finalToken, value);
                return tempToken;
            }
        }
        throw new GenerateTokenIdempotentException("尝试了 " + idempotentProperties.getRetry() + " 次之后,无法生成 token");
    }

    /**
     * 获取 token
     *
     * @return String
     */
    protected abstract String getSubToken();

    /**
     * 持久化信息
     *
     * @param finalToken token
     * @param value      value
     * @return boolean  持久化是否成功
     */
    protected abstract boolean persistence(String finalToken, String value);

    /**
     * 执行检查方法
     * 返回 true 需要拦截,报幂等异常
     * 返回 false 说明不需要拦截,走正常的逻辑
     *
     * @param idempotent 注解元信息
     * @return boolean
     */
    public abstract boolean doCheck(IdempotentMeta idempotent);

    /**
     * 获取 token
     * 最好是写个接口,目前是从 request 获取
     *
     * @return String
     */
    protected String getRequestToken(IdempotentMeta idempotent) {
        String headerToken = request.getHeader(HEAD_KEY);
        if (!StringUtils.hasText(headerToken)) {
            log.debug("header 中没有 token, 返回空");
            return "";
        }
        return idempotentProperties.getRedisKeyPrefix() + request.getHeader(HEAD_KEY);
    }


    private String generalValue() {
        return NetUtil.getLocalhost().getHostAddress();
    }

}
