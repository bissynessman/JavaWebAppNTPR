package tvz.ntpr.core.security;

import org.springframework.stereotype.Component;
import tvz.ntpr.core.helper.JwtHolder;

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
