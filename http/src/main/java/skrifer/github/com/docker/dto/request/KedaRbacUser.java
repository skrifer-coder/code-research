package skrifer.github.com.docker.dto.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class KedaRbacUser {
    private String password;
    private String tenantId;
    private String username;
    private String clientSign;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientSign() {
        return clientSign;
    }

    public void setClientSign(String clientSign) {
        this.clientSign = clientSign;
    }
}
