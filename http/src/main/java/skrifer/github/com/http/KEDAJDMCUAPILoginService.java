package skrifer.github.com.http;

import skrifer.github.com.http.response.KEDAJDAPIBaseResponse;
import skrifer.github.com.http.response.KEDAJDAPILoginResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 登陆认证
 */
public class KEDAJDMCUAPILoginService extends KEDAJDMCUAPIBaseService {
//    //token获取
    public String getToken(String key, String secret, String tokenUrl) {
        Map<String, Object> params = new HashMap<>();
        params.put("oauth_consumer_key", key);
        params.put("oauth_consumer_secret", secret);
        return Optional.ofNullable((KEDAJDAPILoginResponse)post(tokenUrl, params)).orElse(new KEDAJDAPILoginResponse()).getAccount_token();

    }

    //用户登录(需要联合account_token)
    public boolean login(String token, String username, String password, String loginUrl, String usertype) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_token", token);
        params.put("username", username);
        params.put("password", password);
        params.put("user_type", usertype);
        KEDAJDAPIBaseResponse response = post(loginUrl, params);
        return Optional.ofNullable(response).orElse(new KEDAJDAPIBaseResponse()).getSuccess() == 1;
    }

    //保持心跳
    public boolean keepHeartbeat(String token, String url) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_token", token);
        KEDAJDAPIBaseResponse response = post(url, params);
        return Optional.ofNullable(response).orElse(new KEDAJDAPIBaseResponse()).getSuccess() == 1;
    }

}
