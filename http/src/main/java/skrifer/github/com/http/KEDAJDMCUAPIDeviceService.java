package skrifer.github.com.http;

import skrifer.github.com.http.response.KEDAJDAPIDeviceResponse;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 设备信息
 */
public class KEDAJDMCUAPIDeviceService extends KEDAJDMCUAPIBaseService {

    //1.请求指定平台域下的物理服务器设备列表 get /api/v1/nms/platform_domains/{domain_moid}/physicals
    public KEDAJDAPIDeviceResponse getDomainPhysicalDeviceList(String url, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_token", token);
        KEDAJDAPIDeviceResponse kedajdapiBaseResponse = (KEDAJDAPIDeviceResponse)get(url, params);
        return kedajdapiBaseResponse;
    }

    //2.请求物理服务器上的逻辑服务器设备列表 get /api/v1/nms/physicals/{p_server_moid}/logicals
    public KEDAJDAPIDeviceResponse getPhysicalServerLogicalDeviceList(String url, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_token", token);
        KEDAJDAPIDeviceResponse kedajdapiBaseResponse = (KEDAJDAPIDeviceResponse)get(url, params);
        return kedajdapiBaseResponse;
    }

    //3.请求指定平台域下的逻辑服务器设备列表 get /api/v1/nms/platform_domains/{domain_moid}/logicals
    public KEDAJDAPIDeviceResponse getDomainLogicalDeviceList(String url, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_token", token);
        KEDAJDAPIDeviceResponse kedajdapiBaseResponse = (KEDAJDAPIDeviceResponse)get(url, params);
        return kedajdapiBaseResponse;
    }

    //4.请求指定用户域的终端设备列表 get /api/v1/nms/user_domains/{domain_moid}/terminals
    public KEDAJDAPIDeviceResponse getUserDomainDeviceList(String url, String token, String jsonParams) {
        //uelencode jsonParams
        Map<String, Object> params = new HashMap<>();
        params.put("account_token", token);
        params.put("params", URLEncoder.encode(jsonParams));
        KEDAJDAPIDeviceResponse kedajdapiBaseResponse = (KEDAJDAPIDeviceResponse)get(url, params);
        return kedajdapiBaseResponse;
    }


    //5.请求指定物理服务器的详细信息 get /api/v1/nms/physicals/{p_server_moid}/detail
    public KEDAJDAPIDeviceResponse getSinglePhysicalDeviceDetail(String url, String token) {
        //uelencode jsonParams
        Map<String, Object> params = new HashMap<>();
        params.put("account_token", token);
        KEDAJDAPIDeviceResponse kedajdapiBaseResponse = (KEDAJDAPIDeviceResponse)get(url, params);
        return kedajdapiBaseResponse;
    }

    //6.请求非受管终端设备列表
    public KEDAJDAPIDeviceResponse getUnControlDeviceList(String url, String token, int start, int count) {
        //uelencode jsonParams
        Map<String, Object> params = new HashMap<>();
        params.put("account_token", token);
        params.put("start", start);
        params.put("count", count);
        KEDAJDAPIDeviceResponse kedajdapiBaseResponse = (KEDAJDAPIDeviceResponse)get(url, params);
        return kedajdapiBaseResponse;
    }
}
