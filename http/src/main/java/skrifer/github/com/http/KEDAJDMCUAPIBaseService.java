package skrifer.github.com.http;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import skrifer.github.com.http.response.*;

import java.util.Map;

public class KEDAJDMCUAPIBaseService {

    public KEDAJDAPIBaseResponse get(String url, Map<String, Object> params) {
        String response = HttpUtil.get(url, params);
        KEDAJDAPIBaseResponse result = null;
        if (this instanceof KEDAJDMCUAPIAreaService){
            result = JSONUtil.toBean(response, KEDAJDAPIAreaResponse.class);
        } else if (this instanceof KEDAJDMCUAPIDeviceService){
            result = JSONUtil.toBean(response, KEDAJDAPIDeviceResponse.class);
        } else if (this instanceof KEDAJDMCUAPICountService){
            result = JSONUtil.toBean(response, KEDAJDAPICountResponse.class);
        } else if (this instanceof KEDAJDMCUAPIWarningService){
            result = JSONUtil.toBean(response, KEDAJDAPIWarningResponse.class);
        } else if (this instanceof KEDAJDMCUAPIMeetingService){
            result = JSONUtil.toBean(response, KEDAJDAPIMeetingResponse.class);
        }

        if (result.getSuccess() == 1) {
            return result;
        } else {

            System.err.println("返回异常:" + response);
            return null;
        }
    }

    public KEDAJDAPIBaseResponse post(String url, Map<String, Object> params) {
        String response = HttpUtil.post(url, params);
        KEDAJDAPIBaseResponse result = null;
        if (this instanceof KEDAJDMCUAPILoginService) {
            result = JSONUtil.toBean(response, KEDAJDAPILoginResponse.class);
        }
        if (result.getSuccess() == 1) {
            return result;
        } else {
            System.err.println("返回异常:" + response);
            return null;
        }
    }
}
