package com.matrixboot.excel;

import org.springframework.context.annotation.Bean;

/**
 * excel 配置 bean 的类
 * <p>
 * create in 2021/12/15 8:35 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
public class ExcelConfig {

    @Bean
    public SpringWebMvcConfigurer springWebMvcConfigurer() {
        return new SpringWebMvcConfigurer();
    }

    @Bean
    public ExcelRequestBodyHandlerMethodArgumentResolver excelRequestBodyHandlerMethodArgumentResolver() {
        return new ExcelRequestBodyHandlerMethodArgumentResolver();
    }

    @Bean
    public ExcelResponseBodyHandlerMethodReturnValueHandler excelResponseBodyHandlerMethodReturnValueHandler() {
        return new ExcelResponseBodyHandlerMethodReturnValueHandler();
    }

}
