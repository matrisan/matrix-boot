package com.matrixboot.example.infrastructure;

import com.matrixboot.example.interfaces.dto.QueryData;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

/**
 * TODO
 * <p>
 * create in 2022/1/18 2:52 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Service("ConverterService")
public class DataConverterService {

    public String check(@NotNull QueryData queryData) {
        return queryData.getGroup() + ":" + queryData.getUsername();
    }

}
