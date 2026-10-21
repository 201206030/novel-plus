package com.java2nb.novel.core.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.java2nb.novel.core.utils.NameMaskUtil;

import java.io.IOException;

public class CommentUserNameSerialize extends JsonSerializer<String> {

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(NameMaskUtil.maskIfNeeded(s));
    }
}