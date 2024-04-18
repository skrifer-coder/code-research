package skrifer.github.com.http;

import skrifer.github.com.http.response.KEDAJDAPIMeetingResponse;
import skrifer.github.com.http.response.KEDAJDAPIWarningResponse;

import java.util.HashMap;
import java.util.Map;

public class KEDAJDMCUAPIMeetingService extends KEDAJDMCUAPIBaseService {
    /**
     * get /api/v1/nms/service_domains/{service_moid}/meetings
     * @return
     */
    public KEDAJDAPIMeetingResponse getServerUnrepairedWarnings(String url, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_token", token);
        KEDAJDAPIMeetingResponse kedajdapiBaseResponse = (KEDAJDAPIMeetingResponse)get(url, params);
        return kedajdapiBaseResponse;
    }

    /**
     * get /api/v1/nms/meetings/{conf_id}/terminal_detail
     * @return
     */
    public KEDAJDAPIMeetingResponse getTerminalUnrepairedWarnings(String url, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_token", token);
        KEDAJDAPIMeetingResponse kedajdapiBaseResponse = (KEDAJDAPIMeetingResponse)get(url, params);
        return kedajdapiBaseResponse;
    }

}
