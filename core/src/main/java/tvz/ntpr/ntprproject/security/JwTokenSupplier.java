package tvz.ntpr.ntprproject.security;

import org.springframework.stereotype.Component;
import tvz.ntpr.ntprproject.helper.JwtHolder;

import java.util.function.Supplier;

@Component
public class JwTokenSupplier implements Supplier<JwtHolder> {
    private final JwtHolder currentTokens;

    public JwTokenSupplier(JwtHolder jwtHolder) {
        this.currentTokens = jwtHolder;
    }

    @Override
    public JwtHolder get() {
        return currentTokens;
    }
}
