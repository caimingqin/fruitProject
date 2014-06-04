package app.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static final Log logger = LogFactory.getLog(JsonParser.class);
	
	public static String encode(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonGenerationException e) {
			logger.info("JsonGenerationException " + e.getMessage());
		} catch (JsonMappingException e) {
			logger.info("JsonMappingException " + e.getMessage());
		} catch (IOException e) {
			logger.info("IOException " + e.getMessage());
		}
		return null;
	}

	public static <T> T decode(String content, Class<T> clazzs) {
		try {
			return objectMapper.readValue(content, clazzs);
		} catch (JsonParseException e) {
			logger.info("JsonParseException " + e.getMessage());
		} catch (JsonMappingException e) {
			logger.info("JsonMappingException " + e.getMessage());
		} catch (IOException e) {
			logger.info("IOException " + e.getMessage());
		}
		return null;
	}

	public static <T> T decode(String content, TypeReference<T> typeReference) {
		try {
			return objectMapper.readValue(content, typeReference);
		} catch (JsonParseException e) {
			logger.info("JsonParseException " + e.getMessage());
		} catch (JsonMappingException e) {
			logger.info("JsonMappingException " + e.getMessage());
		} catch (IOException e) {
			logger.info("IOException " + e.getMessage());
		}
		return null;
	}

	public static <T> T decode(InputStream in, Class<T> clazzs) {
		try {
			return objectMapper.readValue(in, clazzs);
		} catch (JsonParseException e) {
			logger.info("JsonParseException " + e.getMessage());
		} catch (JsonMappingException e) {
			logger.info("JsonMappingException " + e.getMessage());
		} catch (IOException e) {
			logger.info("IOException " + e.getMessage());
		}
		return null;
	}
}
