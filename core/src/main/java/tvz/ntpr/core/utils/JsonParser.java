package tvz.ntpr.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static <T> T parseIntoObject(String json, Class<T> clazz) throws Exception {
        if (json == null || json.isEmpty()) return null;
        return objectMapper.readValue(json, clazz);
    }

    public static <T> List<T> parseIntoList(String json, Class<T> clazz) throws Exception {
        if (json == null || json.isEmpty()) return new ArrayList<>();
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }
}
