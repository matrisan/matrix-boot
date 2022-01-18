package com.matrixboot.access.limit.dto;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * TODO
 * <p>
 * create in 2022/1/18 4:04 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
public class AccessLimitContext {

    private Map<Method, AccessLimitMeta> metaMap;


    public AccessLimitContext(Object bean) {

    }


    public AccessLimitMeta getAccessLimitMeta(Method method) {
        if (Objects.isNull(method)) {
            initMetaMap();
        }
        return metaMap.get(method);
    }

    private void initMetaMap() {

    }

    public void execute() {

    }


}


