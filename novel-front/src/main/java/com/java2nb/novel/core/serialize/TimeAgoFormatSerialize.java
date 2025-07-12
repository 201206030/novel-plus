package com.java2nb.novel.core.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.java2nb.novel.core.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Date;

public class TimeAgoFormatSerialize extends JsonSerializer<Date> {

    @Override
    public void serialize(Date s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {

        if (s != null) {
            jsonGenerator.writeString(DateUtil.formatTimeAgo(s));
        }

    }
}