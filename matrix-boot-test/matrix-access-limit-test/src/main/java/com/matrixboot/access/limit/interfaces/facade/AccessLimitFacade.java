package com.matrixboot.access.limit.interfaces.facade;

import com.matrixboot.access.limit.annotation.AccessLimit;
import com.matrixboot.access.limit.annotation.AccessLimits;
import com.matrixboot.access.limit.exception.AccessLimitException;
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

    @GetMapping("example0")
    public QueryData example0(QueryData queryData) {
        return queryData;
    }

    @AccessLimit("#queryData.username")
    @GetMapping("example1")
    public QueryData example1(QueryData queryData) {
        return queryData;
    }

    @AccessLimit("@ConverterService.check(#queryData)")
    @GetMapping("example2")
    public QueryData example2(QueryData queryData) {
        return queryData;
    }

    @AccessLimits({
            @AccessLimit(value = "#queryData.username", times = 2, message = "用户名请求次数超过了限制!"),
            @AccessLimit(value = "#queryData.group", times = 4, message = "用户组请求次数超过了限制!")
    })
    @GetMapping("example3")
    public QueryData example3(QueryData queryData) {
        return queryData;
    }

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
