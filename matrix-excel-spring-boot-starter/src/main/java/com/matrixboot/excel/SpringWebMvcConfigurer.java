package com.matrixboot.excel;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO
 * <p>
 * create in 2021/2/25 11:03 上午
 *
 * @author shishaodong
 * @version 0.0.1
 */

public class SpringWebMvcConfigurer implements WebMvcConfigurer {

    @Resource
    private ExcelRequestBodyHandlerMethodArgumentResolver resolver;

    @Override
    public void addArgumentResolvers(@NotNull List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(resolver);
    }

}
