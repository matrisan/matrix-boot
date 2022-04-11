package com.matrixboot.access.limit.interfaces.facade;

import com.matrixboot.access.limit.annotation.AccessLimit;
import com.matrixboot.access.limit.exception.AccessLimitException;
import com.matrixboot.access.limit.infrastructure.DataConverterService;
import com.matrixboot.access.limit.interfaces.vo.QueryData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * create in 2022/1/18 2:47 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@RestController
public class AccessLimitFacade {

    /**
     * 正常的请求方法
     *
     * @param queryData QueryData
     * @return QueryData
     */
    @GetMapping("example0")
    public QueryData example0(QueryData queryData) {
        return queryData;
    }


    /**
     * 对请求参数 queryData.username 进行限制,默认是 1 分钟 3 次
     * 当触发这个限制时会抛出 {@link AccessLimitException} 异常
     *
     * @param queryData QueryData
     * @return QueryData
     */
    @AccessLimit("#queryData.username")
    @GetMapping("example1")
    public QueryData example1(QueryData queryData) {
        return queryData;
    }

    /**
     * 对请求参数 使用了自定义的参数转发 先从容器中获取一个叫 ConverterService 的 bean 然后使用 check 方法
     * {@link DataConverterService#check(QueryData)}
     *
     * @param queryData QueryData
     * @return QueryData
     */
    @AccessLimit("@ConverterService.check(#queryData)")
    @GetMapping("example2")
    public QueryData example2(QueryData queryData) {
        return queryData;
    }

    /**
     * 对多个参数同时进行限制,
     * queryData.username
     * queryData.group
     *
     * @param queryData QueryData
     * @return QueryData
     */
    @AccessLimit(value = "#queryData.username", times = 2, message = "用户名请求次数超过了限制!")
    @AccessLimit(value = "#queryData.group", times = 4, message = "用户组请求次数超过了限制!")
    @GetMapping("example3")
    public QueryData example3(QueryData queryData) {
        return queryData;
    }

    /**
     * 指定回调方法 即 {@link this#reveal}
     *
     * @param queryData QueryData
     * @return QueryData
     */
    @AccessLimit(value = "#queryData.username", message = "用户名请求次数超过了限制!", times = 4, timeout = 30, recover = "reveal")
    @GetMapping("example4")
    public QueryData example4(QueryData queryData) {
        return queryData;
    }

    @SuppressWarnings("unused")
    public QueryData reveal(QueryData queryData, AccessLimitException exception) {
        return new QueryData();
    }
}
