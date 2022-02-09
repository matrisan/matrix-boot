package com.matrixboot.access.limit.config;

import com.matrixboot.access.limit.annotation.AccessLimit;
import com.matrixboot.access.limit.annotation.AccessLimits;
import com.matrixboot.access.limit.dto.AccessLimitMeta;
import com.matrixboot.access.limit.dto.AccessLimitResult;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * create in 2022/1/18 4:35 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Slf4j
public class AccessLimitRedisServiceImpl implements IAccessLimitService {

    @Setter
    private ExpressionParser expressionParser;

    @Setter
    private ParameterNameDiscoverer parameterNameDiscoverer;

    @Setter
    private StringRedisTemplate stringRedisTemplate;

    @Setter
    private BeanResolver beanResolver;

    @Setter
    private RedisScript<Boolean> redisScript;

    @Setter
    private Environment environment;

    @Setter
    private AccessLimitProperties accessLimitProperties;

    private final StandardEvaluationContext evaluationContext = new StandardEvaluationContext();

    private String[] parameterNames;

    private AccessLimitMeta accessLimitMeta;

    private List<AccessLimitMeta> accessLimitMetas;

    private AccessLimit accessLimit;

    private AccessLimits accessLimits;

    private String methodName;

    public AccessLimitRedisServiceImpl(AccessLimit accessLimit) {
        this.accessLimitMeta = new AccessLimitMeta(accessLimit);
        this.accessLimit = accessLimit;
    }

    public AccessLimitRedisServiceImpl(@NotNull AccessLimits accessLimits) {
        this.accessLimitMetas = Arrays.stream(accessLimits.value()).map(AccessLimitMeta::new).collect(Collectors.toList());
        this.accessLimits = accessLimits;
    }

    @Override
    public AccessLimitResult doCheck(Method method, Object[] args) {
        String[] paramNames = getParameterNames(method);
        setArgs(args, paramNames);
        if (!Objects.isNull(accessLimitMetas) && !accessLimitMetas.isEmpty()) {
            List<AccessLimitResult> list = accessLimitMetas.stream().map(this::mapValues).collect(Collectors.toList());
            for (AccessLimitResult result : list) {
                if (Boolean.TRUE.equals(result.getResult())) {
                    return result;
                }
            }
        }
        if (!Objects.isNull(accessLimitMeta)) {
            AccessLimitResult equals = mapValues(accessLimitMeta);
            return Boolean.TRUE.equals(equals.getResult()) ? equals : DEFAULT;
        }
        return DEFAULT;
    }

    private static final AccessLimitResult DEFAULT = new AccessLimitResult(false, null);

    private AccessLimitResult mapValues(@NotNull AccessLimitMeta meta) {
        String spEl = meta.getValue();
        String value = (String) expressionParser.parseExpression(spEl).getValue(evaluationContext);
        assert value != null;
        Boolean aBoolean = stringRedisTemplate.execute(redisScript, Collections.singletonList(getPrefix() + value), meta.getTimeout(), meta.getTimes());
        if (Boolean.TRUE.equals(aBoolean)) {
            return new AccessLimitResult(true, meta.getMessage());
        }
        log.info("value: {} - {}", value, aBoolean);
        return DEFAULT;
    }


    private void setArgs(Object[] args, String[] parameterNames) {
        for (int i = 0; i < Objects.requireNonNull(parameterNames).length; i++) {
            String paramName = parameterNames[i];
            // 参数名称和参数对象设置到表达式上下文对象里,这样才能通过 #reqVo 这样的写法来引用方法参数
            evaluationContext.setVariable(paramName, args[i]);
        }
    }

    private String[] getParameterNames(Method method) {
        if (Objects.isNull(parameterNames)) {
            parameterNames = parameterNameDiscoverer.getParameterNames(method);
        }
        methodName = method.getName();
        return parameterNames;
    }

    private String fullPrefix;

    private String getPrefix() {
        if (StringUtils.hasText(fullPrefix)) {
            return fullPrefix;
        }

        String prefix = accessLimitProperties.getPrefix();
        String property = environment.getProperty("spring.application.name");
        if (StringUtils.hasText(property)) {
            prefix += property;
        } else {
            prefix += "nameless";
        }
        prefix = prefix + ":" + methodName + ":";
        fullPrefix = prefix;
        return fullPrefix;
    }

    @Override
    public String getReveal() {
        return Objects.isNull(accessLimits) ? accessLimit.recover() : accessLimits.recover();
    }

    @Override
    public void afterPropertiesSet() {
        evaluationContext.setBeanResolver(beanResolver);
    }

}
