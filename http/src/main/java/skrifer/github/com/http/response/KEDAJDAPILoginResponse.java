package skrifer.github.com.http.response;

import lombok.Data;

@Data
public class KEDAJDAPILoginResponse extends KEDAJDAPIBaseResponse {
    private String account_token;
    private String username;
}
