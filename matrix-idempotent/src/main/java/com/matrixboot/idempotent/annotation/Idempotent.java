package com.matrixboot.idempotent.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * create in 2021/12/16 9:42 AM
 * <p>
 * <p>
 * 使用示例代码
 * 目前支持两种方式:
 * 1.直接从方法参数取数据:如下例子所示
 * {@code
 * <pre>
 *  @Idempotent(value = "#command.domain", timeout = 60, unit = TimeUnit.SECONDS)
 *  public void configCreate(@Valid ConfigCreateCommand command) {
 *      // do something
 *  }
 * </pre>
 * }
 * {@code
 * <pre>
 *  public class ConfigCreateCommand {
 *     String namespace;
 *     String domain;
 *  }
 * </pre>
 * }
 * 2.如果需要对 ConfigCreateCommand 字段做简单的处理,可以写一个 Bean 注入到容器中,并用@符号从容器中引入注意,自定义的返回数据必须是 String 类型
 * {@code
 * <pre>
 * @Idempotent(value = "@idempotentConfigCreateService.convert(#command)", timeout = 60, unit = TimeUnit.SECONDS)
 * public void configCreate(@NotNull @Size(min = 1) List<@Valid ConfigCreateCommand> command) {
 *    // do something
 *  }
 * </pre>
 * }
 *
 *
 * {@code
 * <pre>
 * @Service
 * public class IdempotentConfigCreateService {
 *     public String convert(List<@Valid ConfigCreateCommand> command) {
 *         return command.stream().map(ConfigCreateCommand::getDomain).collect(Collectors.joining());
 *     }
 * }
 * </pre>
 * }
 * 
 * @author shishaodong
 * @version 0.0.1
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Idempotent {

    String value();

    int timeout() default 1;

    TimeUnit unit() default TimeUnit.MINUTES;

}
