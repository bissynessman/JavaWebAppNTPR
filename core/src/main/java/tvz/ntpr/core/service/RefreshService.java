package tvz.ntpr.core.service;

import tvz.ntpr.core.helper.LoginRequest;

public interface RefreshService {
    String getRefreshToken(LoginRequest loginRequest);
    String getRefreshTokenForSignup(String username);
    String getAccessToken(String refreshToken);
    String getAccessTokenForCron();
}
