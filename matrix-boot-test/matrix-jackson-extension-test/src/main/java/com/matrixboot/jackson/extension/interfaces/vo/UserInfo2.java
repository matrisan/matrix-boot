package com.matrixboot.jackson.extension.interfaces.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.matrixboot.jackson.serializer.MobileSerializer;
import lombok.Data;

/**
 * <p>
 * create in 2022/4/11 23:45
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Data
public class UserInfo2 {

    private String username;

    @JsonSerialize(using = MobileSerializer.class)
    private String mobile;

}
