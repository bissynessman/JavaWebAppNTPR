package tvz.ntpr.ntprdbrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tvz.ntpr.ntprdbrestapi.auth.JwToken;
import tvz.ntpr.ntprdbrestapi.auth.JwtGenerator;
import tvz.ntpr.ntprdbrestapi.auth.LoginRequest;
import tvz.ntpr.ntprdbrestapi.auth.PasswordUtils;
import tvz.ntpr.ntprdbrestapi.entity.User;
import tvz.ntpr.ntprdbrestapi.service.RefreshTokenService;
import tvz.ntpr.ntprdbrestapi.service.UserService;

import java.util.Date;

@RestController
@RequestMapping("/auth")
public class RefreshController {
    private static final long FIFTEEN_SECONDS = 15000;
    private static final long TEN_MINUTES = 600000;
    private static final long ONE_WEEK = 604800000;

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserService userService;

    @PostMapping
    public String generateRefreshToken(@RequestBody LoginRequest loginRequest) {
        User user = userService.getByUsername(loginRequest.getUsername());
        String password = user.getPassword();
        String salt = user.getPasswordSalt();
        String username = user.getUsername();
        if (PasswordUtils.verifyPassword(password, loginRequest.getPassword(), salt, username)) {
            JwToken refreshToken = JwtGenerator.makeToken(user.getUsername(), ONE_WEEK);
            refreshTokenService.deleteByUsername(user.getUsername());
            return refreshTokenService.create(refreshToken).getToken();
        }

        return null;
    }

    @PostMapping("/signup")
    public String generateRefreshTokenAtSignup(@RequestBody String username) {
        if (username != null && !username.isEmpty() && !username.isBlank()) {
            JwToken refreshToken = JwtGenerator.makeToken(username, ONE_WEEK);
            return refreshTokenService.create(refreshToken).getToken();
        }

        return null;
    }

    @PostMapping("/cron")
    public String generateAccessTokenForCron() {
        return JwtGenerator.makeToken("cronjob", TEN_MINUTES).getToken();
    }

    @PostMapping("/refresh")
    public String refreshAccessToken(@RequestBody String refreshTokenValue) {
        JwToken refreshToken = refreshTokenService.getByToken(refreshTokenValue);
        if (refreshToken != null && refreshToken.getValidUntil().after(new Date()))
            return JwtGenerator.makeToken(refreshToken.getUsername(), FIFTEEN_SECONDS).getToken();

        return null;
    }
}
