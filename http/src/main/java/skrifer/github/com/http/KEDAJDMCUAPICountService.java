package skrifer.github.com.http;

import skrifer.github.com.http.response.KEDAJDAPICountResponse;
import skrifer.github.com.http.response.KEDAJDAPIDeviceResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class KEDAJDMCUAPICountService extends KEDAJDMCUAPIBaseService {
    /**
     * get /api/v1/nms/service_domains/{domain_moid}/terminal_online_statistic
     *
     * @return
     */
    public KEDAJDAPICountResponse getOnlineTerminalCountInfo(String url, String token, String jsonParams) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_token", token);
        if (jsonParams != null) {
            try {
                params.put("params", URLEncoder.encode(jsonParams, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        KEDAJDAPICountResponse kedajdapiBaseResponse = (KEDAJDAPICountResponse) get(url, params);
        return kedajdapiBaseResponse;
    }

}
