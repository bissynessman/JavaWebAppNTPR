package tvz.ntpr.ntprproject.helper;

import org.springframework.stereotype.Component;

@Component
public class JwtHolder {
    private String accessToken;
    private String refreshToken;

    public synchronized String getAccessToken() {
        return accessToken;
    }

    public synchronized void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public synchronized String getRefreshToken() {
        return refreshToken;
    }

    public synchronized void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
