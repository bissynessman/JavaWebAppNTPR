package tvz.ntpr.api.entity;

import tvz.ntpr.api.enums.Role;

public class User extends BaseEntity {
    private String email;
    private String username;
    private String password;
    private String passwordSalt;
    private Role role;
    private String userUuid;

    public User() {
        super(null);
    }

    public User(final String id, final String email, final String username, final String password, final String passwordSalt, final Role role, final String userUuid) {
        super(id);
        this.email = email;
        this.username = username;
        this.password = password;
        this.passwordSalt = passwordSalt;
        this.role = role;
        this.userUuid = userUuid;
    }

    public String getId() {
        return super.getId();
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public Role getRole() {
        return this.role;
    }

    public String getUserUuid() {
        return this.userUuid;
    }

    public void setId(final String id) {
        super.setId(id);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public void setRole(final Role role) {
        this.role = role;
    }

    public void setUserUuid(final String userUuid) {
        this.userUuid = userUuid;
    }
}
