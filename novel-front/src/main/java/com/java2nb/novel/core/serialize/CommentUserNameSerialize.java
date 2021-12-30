package com.java2nb.novel.core.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class CommentUserNameSerialize extends JsonSerializer<String> {

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        if(StringUtils.isNotBlank(s)){
            jsonGenerator.writeString(s.substring(0, 4) + "****" + s.substring(s.length() - 3));
        }

    }
}