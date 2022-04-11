package com.matrixboot.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * 手机号码脱敏
 * <p>
 * create in 2022/4/11 11:22
 *
 * @author shishaodong
 * @version 0.0.1
 */
public class MobileSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String s, @NotNull JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String result = StringUtils.substring(s, 0, 3) + "****" + StringUtils.substring(s, 7, 11);
        jsonGenerator.writeObject(result);
    }
}
