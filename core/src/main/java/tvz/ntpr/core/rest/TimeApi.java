package tvz.ntpr.core.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class TimeApi {
    private static final String API_URL = "https://api.api-ninjas.com/v1/worldtime?timezone=Europe/Zagreb";

    public LocalDateTime getCurrentTime() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", "ql3+i8rS/RDUrUHd591kJg==hG8YGGDTeWNTly2q");

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.GET, request, String.class);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        try {
            map = mapper.readValue(response.getBody(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (map != null) {
            String dateTime = map.get("datetime").replace(" ", "T");

            return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME);
        }

        return LocalDateTime.now();
    }
}
