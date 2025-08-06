package tvz.ntpr.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;

public class JsonParser {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static <T> T parseIntoObject(String json, Class<T> clazz) throws Exception {
        T result = objectMapper.readValue(json, clazz);

        return result;
    }

    public static <T> List<T> parseIntoList(String json, Class<T> clazz) throws Exception {
        List <T> result = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));

        return result;
    }
}
