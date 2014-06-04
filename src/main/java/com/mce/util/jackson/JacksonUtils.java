package com.mce.util.jackson;

import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public final class JacksonUtils
{
  private static final ObjectMapper mapper = new ObjectMapper();

  public static ObjectMapper getInstance() {
    return mapper;
  }

  public static String writeValueString(Object value)
  {
    try {
      return mapper.writeValueAsString(value);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Object readBean(String description, Class<?> class1) {
    try {
      return mapper.readValue(description, class1);
    } catch (Exception e) {
      throw new RuntimeException("JSONParse error[" + description + "]", e);
    }
  }

  public static <T> T readArray(String contents, Class<T> class1) {
    try {
      return mapper.readValue(contents, class1);
    } catch (Exception e) {
      throw new RuntimeException("JSONParse error[" + contents + "]", e);
    }
  }

  public static <T> List<T> convertToList(String des, TypeReference<?> tr)
  {
    try {
      return (List)mapper.readValue(des, tr);
    } catch (Exception e) {
      throw new RuntimeException("JSONParse error[" + des + "]", e);
    }
  }
}




