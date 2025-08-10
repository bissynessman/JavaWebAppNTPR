package tvz.ntpr.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tvz.ntpr.api.auth.JwToken;
import tvz.ntpr.api.auth.JwtGenerator;
import tvz.ntpr.api.auth.LoginRequest;
import tvz.ntpr.api.auth.PasswordUtils;
import tvz.ntpr.api.entity.User;
import tvz.ntpr.api.service.RefreshTokenService;
import tvz.ntpr.api.service.UserService;

import java.util.Date;

import static tvz.ntpr.api.config.Urls.*;

@RestController
@RequestMapping(URL_AUTH)
public class AuthController {
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

    @PostMapping(URL_SIGNUP)
    public String generateRefreshTokenAtSignup(@RequestBody String username) {
        if (username != null && !username.isEmpty() && !username.isBlank()) {
            JwToken refreshToken = JwtGenerator.makeToken(username, ONE_WEEK);
            return refreshTokenService.create(refreshToken).getToken();
        }

        return null;
    }

    @PostMapping(URL_CRON)
    public String generateAccessTokenForCron() {
        return JwtGenerator.makeToken("cronjob", TEN_MINUTES).getToken();
    }

    @PostMapping(URL_REFRESH)
    public String refreshAccessToken(@RequestBody String refreshTokenValue) {
        JwToken refreshToken = refreshTokenService.getByToken(refreshTokenValue);
        if (refreshToken != null && refreshToken.getValidUntil().after(new Date()))
            return JwtGenerator.makeToken(refreshToken.getUsername(), FIFTEEN_SECONDS).getToken();

        return null;
    }
}
