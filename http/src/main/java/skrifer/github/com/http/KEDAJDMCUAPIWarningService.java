package skrifer.github.com.http;

import skrifer.github.com.http.response.KEDAJDAPICountResponse;
import skrifer.github.com.http.response.KEDAJDAPIWarningResponse;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class KEDAJDMCUAPIWarningService extends KEDAJDMCUAPIBaseService {
    /**
     * get /api/v1/nms/servers/{server_moid}/unrepaired_warnings
     * @return
     */
    public KEDAJDAPIWarningResponse getServerUnrepairedWarnings(String url, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_token", token);
        KEDAJDAPIWarningResponse kedajdapiBaseResponse = (KEDAJDAPIWarningResponse)get(url, params);
        return kedajdapiBaseResponse;
    }

    /**
     * get /api/v1/nms/terminals/{terminal_moid}/unrepaired_warnings
     * @return
     */
    public KEDAJDAPIWarningResponse getTerminalUnrepairedWarnings(String url, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_token", token);
        KEDAJDAPIWarningResponse kedajdapiBaseResponse = (KEDAJDAPIWarningResponse)get(url, params);
        return kedajdapiBaseResponse;
    }

}
