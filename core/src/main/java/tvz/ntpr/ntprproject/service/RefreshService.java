package tvz.ntpr.ntprproject.service;

import tvz.ntpr.ntprproject.helper.LoginRequest;

public interface RefreshService {
    String getRefreshToken(LoginRequest loginRequest);
    String getRefreshTokenForSignup(String username);
    String getAccessToken(String refreshToken);
    String getAccessTokenForCron();
}
