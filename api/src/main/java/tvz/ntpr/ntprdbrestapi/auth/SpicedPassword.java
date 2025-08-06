package tvz.ntpr.ntprdbrestapi.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SpicedPassword {
    private String passwordHash;
    private String salt;
}
