package tvz.ntpr.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tvz.ntpr.core.helper.JwtHolder;
import tvz.ntpr.core.helper.LoginRequest;
import tvz.ntpr.core.helper.UserToRegister;
import tvz.ntpr.core.service.RefreshService;

@Service
public class AuthenticationService {
    @Autowired
    private JwtHolder jwtHolder;
    @Autowired
    private RefreshService refreshService;

    public void login(String username, String password) {
        String newToken = refreshService.getRefreshToken(new LoginRequest(username, password));
        jwtHolder.setRefreshToken(newToken);
        refresh();
    }

    public void signup(UserToRegister user) {
        String newToken = refreshService.getRefreshTokenForSignup(user.getUsername());
        jwtHolder.setRefreshToken(newToken);
        refresh();
    }

    public void refresh() {
        String refreshToken = jwtHolder.getRefreshToken();
        if (refreshToken != null) {
            String newToken = refreshService.getAccessToken(refreshToken);
            jwtHolder.setAccessToken(newToken);
        } else {
            jwtHolder.setAccessToken(null);
        }
    }

    public void cron() {
        String cronToken = refreshService.getAccessTokenForCron();
        jwtHolder.setAccessToken(cronToken);
    }
}
