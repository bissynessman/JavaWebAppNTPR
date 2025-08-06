package tvz.ntpr.ntprproject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tvz.ntpr.ntprproject.helper.JwtHolder;
import tvz.ntpr.ntprproject.helper.LoginRequest;
import tvz.ntpr.ntprproject.helper.UserToRegister;
import tvz.ntpr.ntprproject.service.RefreshService;

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
        String newToken = refreshService.getAccessToken(jwtHolder.getRefreshToken());
        jwtHolder.setAccessToken(newToken);
    }

    public void cron() {
        String cronToken = refreshService.getAccessTokenForCron();
        jwtHolder.setAccessToken(cronToken);
    }
}
