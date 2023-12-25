package com.dune.greensupermarketbackend.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

public class CustomDoubleSerializer extends JsonSerializer<Double> {
    @Override
    public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(BigDecimal.valueOf(value).setScale(2, BigDecimal.ROUND_HALF_UP));
    }
}
