package tvz.ntpr.core.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tvz.ntpr.core.helper.LoginRequest;
import tvz.ntpr.core.rest.DatabaseAPI;

@Service
@Primary
public class RefreshServiceImpl implements RefreshService {
    private static final String API_URL = DatabaseAPI.AUTH_API;

    private final RestTemplate restTemplate;

    public RefreshServiceImpl(@Qualifier("tokensRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getRefreshToken(LoginRequest loginRequest) {
        return restTemplate.postForEntity(API_URL, loginRequest, String.class).getBody();
    }

    @Override
    public String getRefreshTokenForSignup(String username) {
        return restTemplate.postForEntity(API_URL + "/signup", username, String.class).getBody();
    }

    @Override
    public String getAccessToken(String refreshToken) {
        return restTemplate.postForEntity(API_URL + "/refresh", refreshToken, String.class).getBody();
    }

    @Override
    public String getAccessTokenForCron() {
        return restTemplate.postForEntity(API_URL + "/cron", null, String.class).getBody();
    }
}
