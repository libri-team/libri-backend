package com.swyp.libri.global.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.swyp.libri.global.error.ErrorResponse;

public class JsonUtils {
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static ObjectMapper objectMapper = new ObjectMapper()
		.registerModule(new JavaTimeModule()
			.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer())
			.addSerializer(LocalDate.class, new LocalDateSerializer())
			.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer())
			.addDeserializer(LocalDate.class, new LocalDateDeserializer())
		)
		.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
		.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
		.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);


	public static ErrorResponse readErrorResponse(String str) {
		try {
			return objectMapper.readValue(str, ErrorResponse.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * 직렬화, 역직렬화 시 날짜에 포맷을 적용하는 메소드
	 */
	public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

		@Override
		public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException {
			gen.writeString(value.format(DATE_TIME_FORMATTER));
		}
	}

	public static class LocalDateSerializer extends JsonSerializer<LocalDate> {
		@Override
		public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			gen.writeString(value.format(DATE_FORMATTER));

		}
	}

	public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

		@Override
		public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			return LocalDateTime.parse(p.getValueAsString(), DATE_TIME_FORMATTER);
		}
	}

	public static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

		@Override
		public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			return LocalDate.parse(p.getValueAsString(), DATE_FORMATTER);
		}
	}

}
